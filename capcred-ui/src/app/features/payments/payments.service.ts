import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, of } from 'rxjs';
import { catchError, map, mergeMap, switchMap } from 'rxjs/operators';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../core/services/auth.service';

export interface PaymentRow {
  id: string;
  loanId?: string;
  client: string;
  value: number;
  dueDate: string;
  status: string;
  raw?: unknown;
}

@Injectable({ providedIn: 'root' })
export class PaymentsService {
  constructor(private http: HttpClient, private mocks: MockDataService, private auth: AuthService) {}

  getPayments(): Observable<PaymentRow[]> {
    if (environment.useMocks) {
      return this.mocks.getPayments() as unknown as Observable<PaymentRow[]>;
    }

    return this.auth.currentUser$.pipe(
      mergeMap((user) => {
        if (!user) return of([]);
        const params: any = user.backendRole === 'CLIENT' ? { userId: user.id } : {};
        return this.http.get<any>(`${environment.apiBaseUrl}/loans`, { params });
      }),
      map((response) => {
        const items = Array.isArray(response)
          ? response
          : Array.isArray(response?.content)
            ? response.content
            : [];
        return items;
      }),
      switchMap((loans: any[]) => {
        if (!loans.length) {
          return of([] as PaymentRow[]);
        }

        const requests = loans.map((loan) =>
          this.http
            .get<any[]>(`${environment.apiBaseUrl}/installments/loan/${loan.id ?? loan.loanId ?? loan.loanID}`)
            .pipe(
              map((installments) =>
                (installments ?? []).map((installment) => this.normalizeInstallment(installment, loan))
              ),
              catchError((error) => {
                console.warn('Falha ao carregar parcelas para o emprestimo.', loan, error);
                return of([] as PaymentRow[]);
              })
            )
        );

        return forkJoin(requests).pipe(map((groups) => groups.flat()));
      }),
      catchError((error) => {
        console.error('Erro ao carregar pagamentos.', error);
        return of([] as PaymentRow[]);
      })
    );
  }

  private normalizeInstallment(installment: any, loan: any): PaymentRow {
    const value = Number(installment?.valueDue ?? installment?.valuePaid ?? 0);
    const dueDate = installment?.dueDate
      ? new Date(installment.dueDate).toISOString().slice(0, 10)
      : '';

    return {
      id:
        installment?.installmentId ??
        installment?.id ??
        (typeof crypto !== 'undefined' && 'randomUUID' in crypto
          ? crypto.randomUUID()
          : Date.now().toString()),
      loanId: loan?.id ?? loan?.loanId,
      client: this.buildClientLabel(loan?.userId),
      value,
      dueDate,
      status: this.mapStatus(installment?.paymentStatus),
      raw: { installment, loan },
    };
  }

  private buildClientLabel(value: any): string {
    if (!value) {
      return 'Cliente CapCred';
    }
    return `Cliente ${String(value).slice(0, 8)}`;
  }

  private mapStatus(status: string | undefined): string {
    switch ((status ?? '').toLowerCase()) {
      case 'paid':
      case 'pago':
        return 'Pago';
      case 'overdue':
      case 'atrasado':
        return 'Atrasado';
      default:
        return 'Pendente';
    }
  }
}
