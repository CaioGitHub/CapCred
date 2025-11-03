import { AuthService, User } from '../../../core/services/auth.service';
import { SharedImports } from './../../../core/shared/imports';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [SharedImports],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss'
})
export class Sidebar {
  private allMenuItems = [
    { icon: 'dashboard', label: 'Dashboard', route: '/dashboard' },
    { icon: 'account_balance', label: 'Empréstimos', route: '/loans' },
    { icon: 'people', label: 'Clientes', route: '/clients', roles: ['ADMIN'] },
    { icon: 'payments', label: 'Pagamentos', route: '/payments' },
    { icon: 'bar_chart', label: 'Relatórios', route: '/reports' },
    { icon: 'settings', label: 'Configurações', route: '/settings' },
  ];
  public menu: any[] = [];
  user$: Observable<User | null>;

  constructor(private auth: AuthService) {
    this.user$ = this.auth.currentUser$;
    this.filterMenuByUserRole();
  }

  private filterMenuByUserRole() {
    this.user$.subscribe({
      next: (user) => {
        if (!user) return;
        this.menu = this.allMenuItems.filter(item => {
          if (!item.roles || item.roles.length === 0) return true;
          return item.roles.includes(user.backendRole);
        });
      }
    });
  }

  logout() {
    this.auth.logout();
  }
}
