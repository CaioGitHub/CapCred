import { NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatCardModule, MatIconModule, NgFor],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {
  stats = [
    { title: 'Total de Empréstimos', value: '247', icon: 'credit_card', trend: '+12% este mês', color: 'primary' },
    { title: 'Valor Total', value: 'R$ 2.4M', icon: 'attach_money', trend: '+8% este mês', color: 'success' },
    { title: 'Pendentes', value: '23', icon: 'pending', trend: 'Aguardando aprovação', color: 'warn' },
    { title: 'Em Atraso', value: '7', icon: 'error', trend: 'Requer atenção', color: 'danger' },
  ];
}
