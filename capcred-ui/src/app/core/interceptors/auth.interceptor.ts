import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { TokenService } from '../services/token.service';

// JWT interceptor (stub). Not enabled in providers yet.
// Enable later in main.ts with:
// provideHttpClient(withInterceptors([authInterceptor]))
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const token = tokenService.getAccessToken();

  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  // TODO: handle 401 -> attempt refresh using refresh token
  return next(req);
};

