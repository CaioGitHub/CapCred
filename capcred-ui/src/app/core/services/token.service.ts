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

  clearTokens() {
    localStorage.removeItem(this.accessKey);
    localStorage.removeItem(this.refreshKey);
  }

  // Optional: decode/validate exp when integrating backend
  // hasValidAccessToken(): boolean { return !!this.getAccessToken(); }
}

