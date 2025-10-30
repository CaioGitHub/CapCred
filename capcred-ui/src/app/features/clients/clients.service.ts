import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, tap, throwError } from 'rxjs';
import { catchError, delay, map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { MockDataService } from '../../core/services/mock-data.service';
import { environment } from '../../../environments/environment';

export interface Client {
  id: string;
  name: string;
  email: string;
  phone: string;
}

export interface CreateClientInput {
  name: string;
  email: string;
  phone: string;
}

@Injectable({ providedIn: 'root' })
export class ClientsService {
  private clientsSubject = new BehaviorSubject<Client[]>([]);
  private initialized = false;

  constructor(private http: HttpClient, private mocks: MockDataService) {}

  getClients(): Observable<Client[]> {
    if (!this.initialized) {
      this.initialized = true;
      this.loadClients().subscribe();
    }
    return this.clientsSubject.asObservable();
  }

  createClient(input: CreateClientInput): Observable<Client> {
    const emailTrimmed = (input.email || '').trim();
    const normalizedEmail = emailTrimmed.toLowerCase();
    const exists = this.clientsSubject.value.some(
      (client) => (client.email || '').trim().toLowerCase() === normalizedEmail
    );

    if (exists) {
      return throwError(() => new Error('Este e-mail já está cadastrado.'));
    }

    if (environment.useMocks) {
      const newClient: Client = {
        id: crypto.randomUUID ? crypto.randomUUID() : Date.now().toString(),
        name: input.name.trim(),
        email: emailTrimmed,
        phone: input.phone.trim(),
      };

      const updated = [...this.clientsSubject.value, newClient];
      this.clientsSubject.next(updated);
      return of(newClient).pipe(delay(300));
    }

    return this.http
      .post<Client>(`${environment.apiBaseUrl}/clients`, input)
      .pipe(
        tap((client) => this.clientsSubject.next([...this.clientsSubject.value, client])),
        catchError((error) => {
          console.warn('Falha ao criar cliente via API, usando fallback local.', error);
          return this.createClientWithFallback(input);
        })
      );
  }

  private loadClients(): Observable<Client[]> {
    if (environment.useMocks) {
      return this.mocks.getClients().pipe(
        map((clients: any[]) =>
          clients.map((client: any, index) => ({
            id: client?.id ?? String(index + 1),
            name: client?.name ?? '',
            email: client?.email ?? '',
            phone: client?.phone ?? '',
          }))
        ),
        tap((clients) => this.clientsSubject.next(clients))
      );
    }

    return this.http.get<Client[]>(`${environment.apiBaseUrl}/clients`).pipe(
      tap((clients) => this.clientsSubject.next(clients)),
      catchError((error) => {
        console.warn('Falha ao carregar clientes via API, usando dados mock.', error);
        return this.mocks.getClients().pipe(
          map((clients: any[]) =>
            clients.map((client: any, index) => ({
              id: client?.id ?? String(index + 1),
              name: client?.name ?? '',
              email: client?.email ?? '',
              phone: client?.phone ?? '',
            }))
          ),
          tap((clients) => this.clientsSubject.next(clients))
        );
      })
    );
  }

  private createClientWithFallback(input: CreateClientInput): Observable<Client> {
    const newClient: Client = {
      id: crypto.randomUUID ? crypto.randomUUID() : Date.now().toString(),
      name: input.name.trim(),
      email: input.email.trim(),
      phone: input.phone.trim(),
    };

    this.clientsSubject.next([...this.clientsSubject.value, newClient]);
    return of(newClient).pipe(delay(300));
  }
}

