import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PaymentsService {
  constructor(private http: HttpClient, private mocks: MockDataService) {}

  getPayments(): Observable<any[]> {
    if (environment.useMocks) {
      return this.mocks.getPayments();
    }
    return this.http.get<any[]>(`${environment.apiBaseUrl}/payments`);
  }
}

