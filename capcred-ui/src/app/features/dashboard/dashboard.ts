import { CommonModule, NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MockDataService } from '../../core/services/mock-data.service';

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
export class Dashboard implements OnInit {
  stats = [
    { title: 'Total de Empréstimos', value: 0, displayValue: '0', icon: 'credit_card', trend: '+12% este mês', color: 'primary' },
    { title: 'Valor Total', value: 0, displayValue: 'R$ 0', icon: 'attach_money', trend: '+8% este mês', color: 'success' },
    { title: 'Pendentes', value: 0, displayValue: '0', icon: 'pending', trend: 'Aguardando aprovação', color: 'warn' },
    { title: 'Em Atraso', value: 7, displayValue: '7', icon: 'error', trend: 'Requer atenção', color: 'danger' },
  ];

  loans: any[] = [];
  displayedColumns = ['client', 'amount', 'installments', 'status', 'actions'];
  quickCalcForm: any;

  constructor(private fb: FormBuilder, private mockData: MockDataService) {
    this.quickCalcForm = this.fb.group({
      value: [10000],
      installments: ['6x'],
    });
  }

  ngOnInit() {
    this.mockData.getLoans().subscribe((loans) => {
      this.loans = loans;

      const totalLoans = loans.length;
      const totalAmount = loans
        .filter((l) => l.status.toLowerCase() === 'aprovado')
        .reduce((sum, l) => sum + l.amount, 0);
      const pendingCount = loans.filter((l) => l.status.toLowerCase() === 'pendente').length;

      this.animateCount(0, totalLoans, 800, (val) => {
        this.stats[0].displayValue = Math.floor(val).toString();
      });

      this.animateCurrencySmart(0, totalAmount, 1200, (val) => {
        this.stats[1].displayValue = 'R$ ' + this.smartCurrencyFormat(val);
      });

      this.animateCount(0, pendingCount, 800, (val) => {
        this.stats[2].displayValue = Math.floor(val).toString();
      });
    });
  }

  /** Anima valores inteiros */
  private animateCount(start: number, end: number, duration: number, onUpdate: (val: number) => void) {
    const startTime = performance.now();

    const step = (currentTime: number) => {
      const progress = Math.min((currentTime - startTime) / duration, 1);
      const eased = this.easeOutCubic(progress);
      const current = start + (end - start) * eased;
      onUpdate(current);
      if (progress < 1) requestAnimationFrame(step);
    };

    requestAnimationFrame(step);
  }

  /** Anima valores monetários */
  private animateCurrencySmart(start: number, end: number, duration: number, onUpdate: (val: number) => void) {
    const startTime = performance.now();

    const step = (currentTime: number) => {
      const progress = Math.min((currentTime - startTime) / duration, 1);
      const eased = this.easeOutCubic(progress);
      const current = start + (end - start) * eased;
      onUpdate(current);
      if (progress < 1) requestAnimationFrame(step);
    };

    requestAnimationFrame(step);
  }

  private smartCurrencyFormat(value: number): string {
    if (value >= 1_000_000_000) {
      return (value / 1_000_000_000).toFixed(1).replace('.', ',') + ' B';
    } else if (value >= 1_000_000) {
      return (value / 1_000_000).toFixed(1).replace('.', ',') + ' M';
    } else if (value >= 1_000) {
      return (value / 1_000).toFixed(1).replace('.', ',') + ' K';
    } else {
      return value.toLocaleString('pt-BR', { minimumFractionDigits: 0 });
    }
  }

  private easeOutCubic(t: number): number {
    return 1 - Math.pow(1 - t, 3);
  }

  calculate() {
    const { value, installments } = this.quickCalcForm.value;
    alert(`Simulação de ${installments} para R$ ${value?.toLocaleString('pt-BR')}`);
  }
}
