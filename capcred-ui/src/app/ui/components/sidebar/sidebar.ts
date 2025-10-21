import { SharedImports } from './../../../core/shared/imports';
import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [SharedImports],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss'
})
export class Sidebar {
  menu = [
    { icon: 'dashboard', label: 'Dashboard', route: '/dashboard' },
    { icon: 'account_balance', label: 'Empréstimos', route: '/loans' },
    { icon: 'people', label: 'Clientes', route: '/clients' },
    { icon: 'payments', label: 'Pagamentos', route: '/payments' },
    { icon: 'bar_chart', label: 'Relatórios', route: '/reports' },
    { icon: 'settings', label: 'Configurações', route: '/settings' },
  ];
}
