import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { PAYMENTS_MOCK } from '../../core/mocks/payments.mock';

@Injectable({ providedIn: 'root' })
export class PaymentsService {
  getPayments(): Observable<any[]> {
    return of(PAYMENTS_MOCK).pipe(delay(500));
  }
}

