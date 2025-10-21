import { Routes } from '@angular/router';
import { ShellLayout } from './ui/layouts/shell-layout/shell-layout';
import { Dashboard } from './features/dashboard/dashboard';

export const routes: Routes = [
  {
    path: '',
    component: ShellLayout,
    children: [
      { path: 'dashboard', component: Dashboard },
      {
        path: 'loans',
        loadComponent: () =>
          import('../app/core/mocks/loans.mock').then((m) => m.LoansListComponent),
      },
      {
        path: 'clients',
        loadComponent: () =>
          import('../app/core/mocks/clients.mock').then((m) => m.ClientsListComponent),
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
];
