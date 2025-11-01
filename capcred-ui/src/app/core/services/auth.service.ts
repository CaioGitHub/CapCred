import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';

interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  role?: string;
}

type DisplayRole = 'Administrador' | 'Cliente';

export interface User {
  id?: string;
  email: string;
  name: string;
  role: DisplayRole;
  backendRole: string;
  avatar?: string;
  isAdmin: boolean;
}

export interface RegisterPayload {
  name: string;
  cpf: string;
  email: string;
  password: string;
  monthlyIncome: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private storageKey = 'user';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  readonly currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    private tokenService: TokenService
  ) {
    this.hydrateUserFromStorage();
  }

  login(email: string, password: string): Observable<User> {
    const payload = { email: email.trim(), password };

    return this.http.post<AuthResponse>(`${environment.apiBaseUrl}/auth/login`, payload).pipe(
      tap((response) => this.handleAuthSuccess(response)),
      map(() => {
        const user = this.currentUserSubject.value;
        if (!user) {
          throw new Error('Nao foi possivel carregar os dados do usuario.');
        }
        return user;
      }),
      catchError((error) => {
        this.clearSession();
        const message =
          error?.status === 401 || error?.status === 403
            ? 'Credenciais invalidas.'
            : 'Nao foi possivel realizar o login. Tente novamente.';
        return throwError(() => new Error(message));
      })
    );
  }

  register(payload: RegisterPayload): Observable<void> {
    const cleanedCpf = payload.cpf.replace(/\D/g, '').slice(0, 11);
    const formattedCpf =
      cleanedCpf.length === 11
        ? cleanedCpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')
        : payload.cpf.trim();

    const body = {
      name: payload.name.trim(),
      cpf: formattedCpf,
      email: payload.email.trim(),
      password: payload.password,
      monthlyIncome: Number(payload.monthlyIncome),
    };

    return this.http.post<AuthResponse>(`${environment.apiBaseUrl}/auth/register`, body).pipe(
      tap(() => this.clearSession()),
      map(() => void 0),
      catchError((error) => {
        const serverMessage = this.extractErrorMessage(error);
        const isEmailInUse = error?.status === 409 || error?.status === 403;
        const message = isEmailInUse
          ? serverMessage || 'Este email ja esta cadastrado.'
          : serverMessage || 'Nao foi possivel completar o cadastro. Tente novamente.';
        return throwError(() => new Error(message));
      })
    );
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    if (this.tokenService.hasValidAccessToken()) {
      this.ensureUserFromToken();
      return true;
    }
    this.clearSession();
    return false;
  }

  getRole(): string | null {
    this.ensureUserFromToken();
    return this.currentUserSubject.value?.backendRole ?? null;
  }

  private handleAuthSuccess(response: AuthResponse): void {
    this.tokenService.setTokens({
      accessToken: response.accessToken,
      refreshToken: response.refreshToken,
    });

    const user = this.buildUserProfile(response.accessToken, response.role);
    if (!user) {
      throw new Error('Nao foi possivel processar a resposta de autenticacao.');
    }

    localStorage.setItem(this.storageKey, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private hydrateUserFromStorage(): void {
    const stored = localStorage.getItem(this.storageKey);
    if (!stored) {
      return;
    }

    try {
      const user: User = JSON.parse(stored);
      if (this.tokenService.hasValidAccessToken()) {
        this.currentUserSubject.next(user);
      } else {
        this.clearSession();
      }
    } catch {
      this.clearSession();
    }
  }

  private ensureUserFromToken(): void {
    if (this.currentUserSubject.value) {
      return;
    }

    const token = this.tokenService.getAccessToken();
    if (!token) {
      return;
    }

    const user = this.buildUserProfile(token);
    if (user) {
      localStorage.setItem(this.storageKey, JSON.stringify(user));
      this.currentUserSubject.next(user);
    }
  }

  private buildUserProfile(accessToken: string, explicitRole?: string): User | null {
    const payload = this.tokenService.decodePayload<{ sub?: string; email?: string; userId?: string; role?: string }>(
      accessToken
    );

    if (!payload) {
      return null;
    }

    const email = payload.email ?? payload.sub ?? '';
    const backendRole = (explicitRole ?? payload.role ?? 'CLIENT').toUpperCase();
    const name = email ? this.toTitleCase(email.split('@')[0] ?? '') : 'Usuario CapCred';

    return {
      id: payload.userId,
      email,
      name,
      role: this.mapDisplayRole(backendRole),
      backendRole,
      avatar: this.buildAvatarUrl(name),
      isAdmin: backendRole === 'ADMIN'
    };
  }

  private mapDisplayRole(role: string): DisplayRole {
    switch (role) {
      case 'ADMIN':
      case 'ADMINISTRATOR':
        return 'Administrador';
      default:
        return 'Cliente';
    }
  }

  private buildAvatarUrl(name: string): string {
    const initials = encodeURIComponent(name || 'CapCred');
    return `https://ui-avatars.com/api/?background=0D8ABC&color=fff&name=${initials}`;
  }

  private toTitleCase(value: string): string {
    if (!value) {
      return value;
    }
    return value
      .split(/[.\s_-]+/)
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  private clearSession(): void {
    localStorage.removeItem(this.storageKey);
    this.tokenService.clearTokens();
    this.currentUserSubject.next(null);
  }

  private extractErrorMessage(error: any): string | undefined {
    if (!error) {
      return undefined;
    }

    const raw = error.error;
    if (typeof raw === 'string' && raw.trim()) {
      return raw.trim();
    }

    if (raw && typeof raw === 'object') {
      return raw.message || raw.detail || raw.error;
    }

    return typeof error.message === 'string' ? error.message : undefined;
  }
}
