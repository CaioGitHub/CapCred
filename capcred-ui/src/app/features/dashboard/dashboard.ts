import { CommonModule, NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
  ],
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

  loans = [
    { name: 'Maria Santos', email: 'maria@email.com', amount: 15000, installments: 12, status: 'Aprovado' },
    { name: 'Carlos Lima', email: 'carlos@email.com', amount: 8500, installments: 6, status: 'Pendente' },
    { name: 'Ana Costa', email: 'ana@email.com', amount: 25000, installments: 24, status: 'Rejeitado' },
  ];

  displayedColumns = ['client', 'amount', 'installments', 'status', 'actions'];

  quickCalcForm: any;

  constructor(private fb: FormBuilder) {
    this.quickCalcForm = this.fb.group({
      value: [10000],
      installments: ['6x'],
    });
  }

  calculate() {
    const { value, installments } = this.quickCalcForm.value;
    alert(`Simulação de ${installments} para R$ ${value?.toLocaleString('pt-BR')}`);
  }
}
