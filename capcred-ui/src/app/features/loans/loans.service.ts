import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, tap } from 'rxjs';
import { delay, map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';

export interface Loan {
  id: string;
  clientId?: string;
  client: string;
  email?: string;
  amount: number;
  installments: number;
  status: string;
  date: string;
}

export interface CreateLoanInput {
  clientId?: string;
  clientName: string;
  clientEmail?: string;
  amount: number;
  installments: number;
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
      status: 'Pendente',
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
      .pipe(tap((created) => this.loansSubject.next([...this.loansSubject.value, created])));
  }

  private loadLoans(): Observable<Loan[]> {
    if (environment.useMocks) {
      return this.mocks.getLoans().pipe(
        map((loans: any[], index) =>
          loans.map((loan: any, idx: number) => ({
            id: loan?.id ?? String(idx + 1),
            clientId: loan?.clientId,
            client: loan?.client ?? 'Cliente',
            email: loan?.email,
            amount: Number(loan?.amount) || 0,
            installments: Number(loan?.installments) || 1,
            status: loan?.status ?? 'Pendente',
            date: loan?.date ?? new Date().toISOString().split('T')[0],
          }))
        ),
        tap((loans) => this.loansSubject.next(loans))
      );
    }

    return this.http.get<Loan[]>(`${environment.apiBaseUrl}/loans`).pipe(
      tap((loans) => this.loansSubject.next(loans))
    );
  }
}

