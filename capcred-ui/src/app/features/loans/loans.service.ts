import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, tap, throwError } from 'rxjs';
import { delay, map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';

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
}

export interface CreateLoanInput {
  clientId?: string;
  clientName: string;
  clientEmail?: string;
  amount: number;
  installments: number;
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

  constructor(private http: HttpClient, private mocks: MockDataService) {}

  getLoans(): Observable<Loan[]> {
    if (!this.initialized) {
      this.initialized = true;
      this.loadLoans().subscribe();
    }
    return this.loansSubject.asObservable();
  }

  createLoan(input: CreateLoanInput): Observable<Loan> {
    const loan: Loan = {
      id: crypto.randomUUID ? crypto.randomUUID() : Date.now().toString(),
      clientId: input.clientId,
      client: input.clientName.trim(),
      email: input.clientEmail?.trim(),
      amount: input.amount,
      installments: input.installments,
      status: LoanStatus.Pendente,
      date: new Date().toISOString().split('T')[0],
    };

    if (environment.useMocks) {
      const updated = [...this.loansSubject.value, loan];
      this.loansSubject.next(updated);
      return of(loan).pipe(delay(300));
    }

    return this.http
      .post<Loan>(`${environment.apiBaseUrl}/loans`, {
        clientId: input.clientId,
        client: input.clientName,
        email: input.clientEmail,
        amount: input.amount,
        installments: input.installments,
      })
      .pipe(
        map((created) => this.normalizeLoan(created, loan)),
        tap((created) => this.loansSubject.next([...this.loansSubject.value, created]))
      );
  }

  updateLoan(id: string, input: UpdateLoanInput): Observable<Loan> {
    if (environment.useMocks) {
      const existing = this.loansSubject.value.find((loan) => loan.id === id);
      if (!existing) {
        return throwError(() => new Error('Empréstimo não encontrado.'));
      }

      const updated: Loan = {
        ...existing,
        amount: input.amount,
        installments: input.installments,
        status: input.status ?? existing.status,
      };

      this.loansSubject.next(
        this.loansSubject.value.map((loan) => (loan.id === id ? updated : loan))
      );

      return of(updated).pipe(delay(300));
    }

    const existing = this.loansSubject.value.find((loan) => loan.id === id);

    return this.http
      .put<Loan>(`${environment.apiBaseUrl}/loans/${id}`, input)
      .pipe(
        map((loan) => this.normalizeLoan(loan, existing ?? { id, ...input })),
        tap((updated) =>
          this.loansSubject.next(
            this.loansSubject.value.map((loan) => (loan.id === id ? updated : loan))
          )
        )
      );
  }

  private loadLoans(): Observable<Loan[]> {
    if (environment.useMocks) {
      return this.mocks.getLoans().pipe(
        map((loans: any[]) =>
          loans.map((loan: any, idx: number) => ({
            id: loan?.id ?? String(idx + 1),
            clientId: loan?.clientId ?? loan?.email ?? `client-${idx + 1}`,
            client: loan?.client ?? 'Cliente',
            email: loan?.email,
            amount: Number(loan?.amount) || 0,
            installments: Number(loan?.installments) || 1,
            status: this.parseStatus(loan?.status),
            date: loan?.date ?? new Date().toISOString().split('T')[0],
          }))
        ),
        tap((loans) => this.loansSubject.next(loans))
      );
    }

    return this.http.get<Loan[]>(`${environment.apiBaseUrl}/loans`).pipe(
      map((loans) => loans.map((loan) => this.normalizeLoan(loan))),
      tap((loans) => this.loansSubject.next(loans))
    );
  }

  private parseStatus(value: any): LoanStatus {
    const normalized = String(value ?? '').toLowerCase();
    switch (normalized) {
      case 'aprovado':
        return LoanStatus.Aprovado;
      case 'rejeitado':
        return LoanStatus.Rejeitado;
      case 'cancelado':
        return LoanStatus.Cancelado;
      case 'pendente':
      default:
        return LoanStatus.Pendente;
    }
  }

  private normalizeLoan(raw: any, fallback?: Partial<Loan>): Loan {
    return {
      id: raw?.id ?? fallback?.id ?? crypto.randomUUID?.() ?? Date.now().toString(),
      clientId: raw?.clientId ?? fallback?.clientId ?? raw?.email ?? fallback?.email,
      client: raw?.client ?? fallback?.client ?? 'Cliente',
      email: raw?.email ?? fallback?.email,
      amount: Number(raw?.amount ?? fallback?.amount ?? 0),
      installments: Number(raw?.installments ?? fallback?.installments ?? 1),
      status: this.parseStatus(raw?.status ?? fallback?.status),
      date: raw?.date ?? fallback?.date ?? new Date().toISOString().split('T')[0],
    };
  }
}

