import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ClientsService {
  constructor(private http: HttpClient, private mocks: MockDataService) {}

  getClients(): Observable<any[]> {
    if (environment.useMocks) {
      return this.mocks.getClients();
    }
    return this.http.get<any[]>(`${environment.apiBaseUrl}/clients`);
  }
}

