import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { REPORTS_MOCK } from '../../core/mocks/reports.mock';

@Injectable({ providedIn: 'root' })
export class ReportsService {
  getReports(): Observable<any[]> {
    return of(REPORTS_MOCK).pipe(delay(500));
  }
}

