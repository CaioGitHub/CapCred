import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { provideAnimations } from '@angular/platform-browser/animations';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { LOCALE_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';

registerLocaleData(localePt);

bootstrapApplication(App, {
  ...appConfig,
  providers: [
    provideRouter(routes),
    provideAnimations(),
    { provide: LOCALE_ID, useValue: 'pt-BR' } // 👈 aqui o segredo
  ],
}).catch(err => console.error(err));
