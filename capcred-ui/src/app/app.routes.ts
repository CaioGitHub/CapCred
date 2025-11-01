import { Routes } from '@angular/router';
import { ShellLayout } from './ui/layouts/shell-layout/shell-layout';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/login/login';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    component: Login,
  },
  {
    path: '',
    component: ShellLayout,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: Dashboard },
      {
        path: 'loans',
        loadComponent: () =>
          import('./features/loans/loans').then((m) => m.Loans),
      },
      {
        path: 'clients',
        loadComponent: () =>
          import('./features/clients/clients').then((m) => m.Clients),
      },
      {
        path: 'payments',
        loadComponent: () =>
          import('./features/payments/payments').then((m) => m.Payments),
      },
      {
        path: 'reports',
        loadComponent: () =>
          import('./features/reports/reports').then((m) => m.Reports),
      },
      {
        path: 'settings',
        loadComponent: () =>
          import('./features/settings/settings').then((m) => m.Settings),
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },

  { path: '**', redirectTo: 'login' },
];
