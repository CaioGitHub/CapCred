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
  getClients() {
    return of(CLIENTS_MOCK).pipe(delay(500));
  }

  getLoans() {
    return of(LOANS_MOCK).pipe(delay(500));
  }

  getPayments() {
    return of(PAYMENTS_MOCK).pipe(delay(500));
  }

  getReports() {
    return of(REPORTS_MOCK).pipe(delay(500));
  }
}
