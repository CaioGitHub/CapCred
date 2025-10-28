import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { provideAnimations } from '@angular/platform-browser/animations';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { LOCALE_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
// When ready to enable the JWT interceptor, switch to:
// import { provideHttpClient, withInterceptors } from '@angular/common/http';
// import { authInterceptor } from './app/core/interceptors/auth.interceptor';
import { routes } from './app/app.routes';

registerLocaleData(localePt);

bootstrapApplication(App, {
  ...appConfig,
  providers: [
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(), // prepared for API integration
    // To enable the interceptor later:
    // provideHttpClient(withInterceptors([authInterceptor])),
    { provide: LOCALE_ID, useValue: 'pt-BR' } // 👈 aqui o segredo
  ],
}).catch(err => console.error(err));
