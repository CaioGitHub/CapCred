import { Injectable } from '@angular/core';

// Simple token storage service. Replace with a more secure strategy if needed.
@Injectable({ providedIn: 'root' })
export class TokenService {
  private accessKey = 'access_token';
  private refreshKey = 'refresh_token';

  setTokens(tokens: { accessToken?: string; refreshToken?: string }) {
    if (tokens.accessToken !== undefined) {
      localStorage.setItem(this.accessKey, tokens.accessToken);
    }
    if (tokens.refreshToken !== undefined) {
      localStorage.setItem(this.refreshKey, tokens.refreshToken);
    }
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.accessKey);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.refreshKey);
  }

  hasValidAccessToken(): boolean {
    const token = this.getAccessToken();
    if (!token) {
      return false;
    }

    const payload = this.decodePayload(token);
    const exp = (payload as Record<string, any>)?.['exp'];
    if (exp === undefined || exp === null) {
      return true;
    }

    const expirationMs = Number(exp) * 1000;
    return Date.now() < expirationMs;
  }

  clearTokens() {
    localStorage.removeItem(this.accessKey);
    localStorage.removeItem(this.refreshKey);
  }

  decodePayload<T = Record<string, unknown>>(token: string): T | null {
    try {
      const [_, payload] = token.split('.');
      if (!payload) {
        return null;
      }
      const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(decoded) as T;
    } catch {
      return null;
    }
  }
}

