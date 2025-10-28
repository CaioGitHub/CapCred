import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { LOANS_MOCK } from '../../core/mocks/loans.mock';

@Injectable({ providedIn: 'root' })
export class LoansService {
  getLoans(): Observable<any[]> {
    return of(LOANS_MOCK).pipe(delay(500));
  }
}

