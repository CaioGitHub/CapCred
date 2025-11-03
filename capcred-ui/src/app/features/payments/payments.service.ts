import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, of, timeout } from 'rxjs';
import { catchError, map, mergeMap, switchMap, take } from 'rxjs/operators';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../core/services/auth.service';
import { ClientsService } from '../clients/clients.service';

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
  constructor(
    private http: HttpClient,
    private mocks: MockDataService,
    private auth: AuthService,
    private clientsService: ClientsService
  ) {}

  getPayments(): Observable<PaymentRow[]> {
    if (environment.useMocks) {
      return this.mocks.getPayments() as unknown as Observable<PaymentRow[]>;
    }

    return this.auth.currentUser$.pipe(
      mergeMap((user) => {
        if (!user) return of({ loans: [], clients: [] });
        const params: any = user.isAdmin ? {} : { userId: user.id };

        // Buscar loans e clients em paralelo com timeout reduzido
        return forkJoin({
          loans: this.http.get<any>(`${environment.apiBaseUrl}/loans`, { params }).pipe(
            timeout(10000),
            catchError((error) => {
              console.error('Erro ao buscar loans:', error);
              return of([]);
            })
          ),
          clients: this.clientsService.getClients().pipe(
            take(1), // Pega apenas o primeiro valor e completa
            timeout(10000),
            catchError((error) => {
              console.error('Erro ao buscar clients:', error);
              return of([]);
            })
          )
        });
      }),
      map(({ loans, clients }: { loans: any; clients: any[] }) => {
        const items = Array.isArray(loans)
          ? loans
          : Array.isArray(loans?.content)
            ? loans.content
            : [];

        // Criar um mapa de userId -> client name
        const clientsMap: Map<string, string> = new Map(
          clients.map((c: any) => [c.id as string, c.name as string])
        );

        return { loans: items, clientsMap };
      }),
      switchMap(({ loans, clientsMap }) => {
        if (!loans.length) {
          return of([] as PaymentRow[]);
        }

        const requests = loans.map((loan: any) =>
          this.http
            .get<any[]>(
              `${environment.apiBaseUrl}/installments/loan/${loan.id ?? loan.loanId ?? loan.loanID}`
            )
            .pipe(
              map((installments) =>
                (installments ?? []).map((installment) =>
                  this.normalizeInstallment(installment, loan, clientsMap)
                )
              ),
              catchError((error) => {
                console.warn('Falha ao carregar parcelas para o emprestimo.', loan, error);
                return of([] as PaymentRow[]);
              })
            )
        );

        // Se não há requests, retornar array vazio (forkJoin([]) nunca emite!)
        if (requests.length === 0) {
          return of([] as PaymentRow[]);
        }

        return (forkJoin(requests) as Observable<PaymentRow[][]>).pipe(
          map((groups) => groups.flat())
        );
      }),
      catchError((error) => {
        console.error('Erro ao carregar pagamentos.', error);
        return of([] as PaymentRow[]);
      })
    );
  }

  private normalizeInstallment(installment: any, loan: any, clientsMap: Map<string, string>): PaymentRow {
    const value = Number(installment?.valueDue ?? installment?.valuePaid ?? 0);
    const dueDate = installment?.dueDate
      ? new Date(installment.dueDate).toISOString().slice(0, 10)
      : '';

    const clientName = clientsMap.get(loan?.userId) || `Cliente ${String(loan?.userId).slice(0, 8)}`;

    return {
      id:
        installment?.installmentId ??
        installment?.id ??
        (typeof crypto !== 'undefined' && 'randomUUID' in crypto
          ? crypto.randomUUID()
          : Date.now().toString()),
      loanId: loan?.id ?? loan?.loanId,
      client: clientName,
      value,
      dueDate,
      status: this.mapStatus(installment?.paymentStatus),
      raw: { installment, loan },
    };
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

  /**
   * Realiza o pagamento de uma parcela
   * @param installmentId ID da parcela a ser paga
   * @returns Observable com a resposta da API
   */
  payInstallment(installmentId: string): Observable<any> {
    return this.http.post(`${environment.apiBaseUrl}/installments/${installmentId}/pay`, {}).pipe(
      timeout(15000),
      catchError((error) => {
        console.error('Erro ao processar pagamento:', error);
        throw error;
      })
    );
  }
}
