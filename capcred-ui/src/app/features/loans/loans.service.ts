import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';
import { catchError, map, mergeMap, switchMap, tap } from 'rxjs/operators';
import { AuthService } from '../../core/services/auth.service';

export enum LoanStatus {
  Pendente = 'Pendente',
  Aprovado = 'Aprovado',
  Rejeitado = 'Rejeitado',
  Cancelado = 'Cancelado',
}

export interface Loan {
  id: string;
  clientId?: string;
  client: string;
  email?: string;
  amount: number;
  installments: number;
  status: LoanStatus;
  date: string;
  monthlyInstallmentValue?: number;
  requestStatus?: string;
  loanStatus?: string;
}

export interface CreateLoanInput {
  clientId: string;
  clientName: string;
  clientEmail?: string;
  amount: number;
  installments: number;
  firstDueDate: string;
}

export interface UpdateLoanInput {
  amount: number;
  installments: number;
  status?: LoanStatus;
}

@Injectable({ providedIn: 'root' })
export class LoansService {
  private loansSubject = new BehaviorSubject<Loan[]>([]);
  private initialized = false;

  constructor(private http: HttpClient, private mocks: MockDataService, private auth: AuthService) {}

  getLoans(): Observable<Loan[]> {
    if (!this.initialized) {
      this.initialized = true;
      this.loadLoans().subscribe({
        error: (error) => console.error('Nao foi possivel carregar os emprestimos.', error),
      });
    }
    return this.loansSubject.asObservable();
  }

  createLoan(input: CreateLoanInput): Observable<Loan> {
    if (environment.useMocks) {
      return this.createLoanWithMocks(input);
    }
    return this.createLoanFromApi(input);
  }

  updateLoan(id: string, input: UpdateLoanInput): Observable<Loan> {
    if (environment.useMocks) {
      return this.updateLoanWithMocks(id, input);
    }
    return throwError(
      () => new Error('Atualizacao de emprestimo ainda nao e suportada pelo servico.')
    );
  }

  private loadLoans(): Observable<Loan[]> {
    if (environment.useMocks) {
      return this.mocks.getLoans().pipe(
        map((loans: any[]) =>
          loans.map((loan: any, idx: number) =>
            this.normalizeLoan(
              loan,
              {
                id: String(idx + 1),
                clientId: loan?.clientId ?? loan?.email ?? `client-${idx + 1}`,
                client: loan?.client ?? 'Cliente',
                email: loan?.email,
              },
              true
            )
          )
        ),
        tap((loans) => this.loansSubject.next(loans))
      );
    }

    return this.syncLoansFromApi();
  }

  private createLoanWithMocks(input: CreateLoanInput): Observable<Loan> {
    const newLoan: Loan = {
      id: this.generateId(),
      clientId: input.clientId,
      client: input.clientName.trim(),
      email: input.clientEmail?.trim(),
      amount: input.amount,
      installments: input.installments,
      status: LoanStatus.Pendente,
      date: input.firstDueDate,
      monthlyInstallmentValue: this.calculateInstallmentValue(input.amount, input.installments),
    };

    const updated = [...this.loansSubject.value, newLoan];
    this.loansSubject.next(updated);
    return of(newLoan);
  }

  private updateLoanWithMocks(id: string, input: UpdateLoanInput): Observable<Loan> {
    const existing = this.loansSubject.value.find((loan) => loan.id === id);
    if (!existing) {
      return throwError(() => new Error('Emprestimo nao encontrado.'));
    }

    const updated: Loan = {
      ...existing,
      amount: input.amount,
      installments: input.installments,
      status: input.status ?? existing.status,
      monthlyInstallmentValue: this.calculateInstallmentValue(input.amount, input.installments),
    };

    this.loansSubject.next(
      this.loansSubject.value.map((loan) => (loan.id === id ? updated : loan))
    );

    return of(updated);
  }

  private createLoanFromApi(input: CreateLoanInput): Observable<Loan> {
    const payload = {
      userId: input.clientId,
      requestedAmount: input.amount,
      termInMonths: input.installments,
      firstDueDate: input.firstDueDate,
    };

    return this.http.post(`${environment.apiBaseUrl}/loans/request`, payload).pipe(
      switchMap(() => this.syncLoansFromApi()),
      map((loans) => this.matchCreatedLoan(loans, input)),
      catchError((error) => {
        console.error('Erro ao criar emprestimo.', error);
        return throwError(() => new Error('Nao foi possivel criar o emprestimo.'));
      })
    );
  }

  private syncLoansFromApi(): Observable<Loan[]> {
    return this.fetchLoansFromApi().pipe(
      tap((loans) => this.loansSubject.next(loans)),
      catchError((error) => {
        console.error('Erro ao carregar emprestimos do servico.', error);
        return of([] as Loan[]);
      })
    );
  }

  private fetchLoansFromApi(): Observable<Loan[]> {
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
        return items.map((item: any) => this.normalizeLoan(item));
      })
    );
  }

  private matchCreatedLoan(loans: Loan[], input: CreateLoanInput): Loan {
    const match = loans.find(
      (loan) =>
        loan.clientId === input.clientId &&
        this.nearlyEqual(loan.amount, input.amount) &&
        loan.installments === input.installments &&
        this.sameDate(loan.date, input.firstDueDate)
    );

    return (
      match ??
      this.normalizeLoan(
        {
          userId: input.clientId,
          requestedAmount: input.amount,
          termInMonths: input.installments,
          firstDueDate: input.firstDueDate,
          requestStatus: 'PENDING',
        },
        {
          client: input.clientName,
          email: input.clientEmail,
        }
      )
    );
  }

  private normalizeLoan(raw: any, fallback?: Partial<Loan>, fromMocks = false): Loan {
    const amount = Number(
      raw?.requestedAmount ?? raw?.amount ?? fallback?.amount ?? (fromMocks ? raw?.value : 0)
    );
    const installments = Number(
      raw?.termInMonths ?? raw?.installments ?? fallback?.installments ?? 1
    );
    const firstDueDate =
      raw?.firstDueDate ??
      raw?.date ??
      fallback?.date ??
      new Date().toISOString().split('T')[0];

    const requestStatus = raw?.requestStatus ?? raw?.status ?? fallback?.status;

    return {
      id: raw?.id ?? fallback?.id ?? this.generateId(),
      clientId: raw?.userId ?? fallback?.clientId ?? fallback?.email,
      client: fallback?.client ?? this.buildClientLabel(raw?.client ?? raw?.userId),
      email: fallback?.email ?? raw?.email,
      amount,
      installments,
      status: this.parseStatus(requestStatus),
      date: firstDueDate,
      monthlyInstallmentValue: Number(
        raw?.monthlyInstallmentValue ??
          (installments > 0 ? amount / installments : amount) ??
          fallback?.monthlyInstallmentValue ??
          0
      ),
      requestStatus: raw?.requestStatus ?? fallback?.requestStatus,
      loanStatus: raw?.loanStatus ?? fallback?.loanStatus,
    };
  }

  private parseStatus(value: any): LoanStatus {
    const normalized = String(value ?? '').toLowerCase();
    switch (normalized) {
      case 'approved':
      case 'aprovado':
        return LoanStatus.Aprovado;
      case 'rejected':
      case 'rejeitado':
        return LoanStatus.Rejeitado;
      case 'cancelado':
      case 'cancelled':
      case 'canceled':
        return LoanStatus.Cancelado;
      default:
        return LoanStatus.Pendente;
    }
  }

  private buildClientLabel(value: any): string {
    if (!value) {
      return 'Cliente CapCred';
    }
    if (typeof value === 'string') {
      return value;
    }
    return `Cliente ${String(value).slice(0, 8)}`;
  }

  private generateId(): string {
    return typeof crypto !== 'undefined' && 'randomUUID' in crypto
      ? crypto.randomUUID()
      : Date.now().toString();
  }

  private calculateInstallmentValue(amount: number, installments: number): number {
    return installments > 0 ? Number((amount / installments).toFixed(2)) : amount;
  }

  private nearlyEqual(a: number, b: number, tolerance = 0.01): boolean {
    return Math.abs(a - b) <= tolerance;
  }

  private sameDate(a: string, b: string): boolean {
    return new Date(a).toISOString().slice(0, 10) === new Date(b).toISOString().slice(0, 10);
  }
}
