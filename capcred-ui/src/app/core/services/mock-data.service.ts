import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { CLIENTS_MOCK } from '../mocks/clients.mock';
import { LOANS_MOCK } from '../mocks/loans.mock';
import { PAYMENTS_MOCK } from '../mocks/payments.mock';
import { REPORTS_MOCK } from '../mocks/reports.mock';

@Injectable({
  providedIn: 'root',
})
export class MockDataService {
  constructor() {}

  // Simula um delay de rede para maior realismo
  private simulateDelay<T>(data: T, ms = 400): Observable<T> {
    return of(data).pipe(delay(ms));
  }

  /** 👥 Clientes */
  getClients(): Observable<any[]> {
    return this.simulateDelay(CLIENTS_MOCK);
  }

  /** 💰 Empréstimos */
  getLoans(): Observable<any[]> {
    return this.simulateDelay(LOANS_MOCK);
  }

  /** 💳 Pagamentos */
  getPayments(): Observable<any[]> {
    return this.simulateDelay(PAYMENTS_MOCK);
  }

  /** 📊 Relatórios */
  getReports(): Observable<any[]> {
    return this.simulateDelay(REPORTS_MOCK);
  }
}
