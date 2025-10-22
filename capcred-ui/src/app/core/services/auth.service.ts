import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { delay, tap } from 'rxjs/operators';

export interface User {
  email: string;
  name: string;
  role: 'Administrador' | 'Analista' | 'Financeiro';
  avatar?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private router: Router) {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  login(email: string, password: string): Observable<User | null> {
    const fakeUsers: User[] = [
      {
        email: 'admin@capcred.com',
        role: 'Administrador',
        name: 'João Silva',
        avatar: 'https://i.pravatar.cc/150?img=3',
      },
      {
        email: 'analista@capcred.com',
        role: 'Analista',
        name: 'Carlos Lima',
        avatar: 'https://i.pravatar.cc/150?img=5',
      },
      {
        email: 'financeiro@capcred.com',
        role: 'Financeiro',
        name: 'Ana Costa',
        avatar: 'https://i.pravatar.cc/150?img=6',
      },
    ];

    const foundUser = fakeUsers.find((u) => u.email === email);

    return of(foundUser ?? null).pipe(
      delay(800),
      tap((user) => {
        if (user) {
          localStorage.setItem('user', JSON.stringify(user));
          this.currentUserSubject.next(user);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return !!this.currentUserSubject.value;
  }

  getRole(): string | null {
    return this.currentUserSubject.value?.role ?? null;
  }
}
