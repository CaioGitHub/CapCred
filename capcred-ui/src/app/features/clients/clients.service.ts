import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { CLIENTS_MOCK } from '../../core/mocks/clients.mock';

@Injectable({ providedIn: 'root' })
export class ClientsService {
  getClients(): Observable<any[]> {
    return of(CLIENTS_MOCK).pipe(delay(500));
  }
}

