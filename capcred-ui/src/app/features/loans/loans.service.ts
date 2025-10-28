import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class LoansService {
  constructor(private http: HttpClient, private mocks: MockDataService) {}

  getLoans(): Observable<any[]> {
    if (environment.useMocks) {
      return this.mocks.getLoans();
    }
    return this.http.get<any[]>(`${environment.apiBaseUrl}/loans`);
  }
}

