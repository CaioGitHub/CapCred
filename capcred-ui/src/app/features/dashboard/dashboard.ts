import { CommonModule, NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { LoansService, SimulatedLoan } from '../loans/loans.service';
import { MatTooltip } from '@angular/material/tooltip';
import { Router } from '@angular/router';
import { PaymentsService } from '../payments/payments.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CreateClientDialog } from '../clients/create-client-dialog/create-client-dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService, User } from '../../core/services/auth.service';
import { Observable } from 'rxjs';
import { SimulateLoanDialog } from '../loans/simulate-loan-dialog/simulate-loan-dialog';
import moment from 'moment';
import { MatDatepicker, MatDatepickerInput, MatDatepickerModule, MatDatepickerToggle } from "@angular/material/datepicker";
import { MatNativeDateModule } from '@angular/material/core';

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
    MatTooltip,
    MatDialogModule,
    MatSnackBarModule,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatDatepickerModule,
    MatNativeDateModule,
],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  stats = [
    { title: 'Total de Empréstimos', value: 0, displayValue: '0', icon: 'credit_card', color: 'primary' },
    { title: 'Valor Total', value: 0, displayValue: 'R$ 0', icon: 'attach_money', color: 'success' },
    { title: 'Pendentes', value: 0, displayValue: '0', icon: 'pending', color: 'warn' },
    { title: 'Em Atraso', value: 0, displayValue: '0', icon: 'error', color: 'danger' },
  ];

  loans: any[] = [];
  recentLoans: any[] = [];
  displayedColumns = ['client', 'amount', 'installments', 'status', 'date', 'actions'];
  quickCalcForm: any;
  // Pagamentos (Status de Pagamentos)
  emDiaCount = 0;
  vencendoCount = 0;
  emAtrasoCount = 0;

  user$: Observable<User | null>;

  constructor(
    private fb: FormBuilder,
    private loansService: LoansService,
    private router: Router,
    private paymentsService: PaymentsService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private auth: AuthService
  ) {
    this.user$ = this.auth.currentUser$;
    this.quickCalcForm = this.fb.group({
      value: [10000],
      installments: ['6x'],
      firstDueDate: [moment().add(1, 'months').toDate(), Validators.required],
    });
  }

  ngOnInit() {
    this.loansService.getLoans().subscribe((loans) => {
      // Ordena por data (mais recente primeiro)
      this.loans = loans.sort(
        (a, b) => new Date(b.date).getTime() - new Date(a.date).getTime()
      );

      // Mantém apenas os 5 mais recentes
      this.recentLoans = this.loans.slice(0, 5);

      // KPIs
      const totalLoans = loans.length;
      const totalAmount = loans
        .filter((l) => l.status.toLowerCase() === 'aprovado')
        .reduce((sum, l) => sum + l.amount, 0);
      const pendingCount = loans.filter((l) => l.status.toLowerCase() === 'pendente').length;

      // Animações
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

    // Status de Pagamentos (dados reais do service de payments)
    this.paymentsService.getPayments().subscribe((payments) => {
      const toLower = (s: string) => (s || '').toLowerCase();
      const emDia = payments.filter((p) => toLower(p.status) === 'pago').length;
      const vencendo = payments.filter((p) => toLower(p.status) === 'pendente').length;
      const emAtraso = payments.filter((p) => toLower(p.status) === 'em atraso').length;

      // Atualiza contadores
      this.emDiaCount = emDia;
      this.vencendoCount = vencendo;
      this.emAtrasoCount = emAtraso;

      this.stats[3].value = emAtraso;
      this.animateCount(0, emAtraso, 800, (val) => {
        this.stats[3].displayValue = Math.floor(val).toString();
      });
    });
  }

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
    if (value >= 1_000_000_000) return (value / 1_000_000_000).toFixed(1).replace('.', ',') + ' B';
    if (value >= 1_000_000) return (value / 1_000_000).toFixed(1).replace('.', ',') + ' M';
    if (value >= 1_000) return (value / 1_000).toFixed(1).replace('.', ',') + ' K';
    return value.toLocaleString('pt-BR', { minimumFractionDigits: 0 });
  }

  private easeOutCubic(t: number): number {
    return 1 - Math.pow(1 - t, 3);
  }

  goToLoans() {
    this.router.navigate(['/loans'], { queryParams: { from: 'dashboard' } });
  }

  calculate() {
    const { value, installments, firstDueDate } = this.quickCalcForm.value;
    this.loansService.simulateLoan({
      amount: Number(value) || 0,
      installments: Number(String(installments).replace(/\D/g, '')) || 1,
      firstDueDate: firstDueDate.toISOString().slice(0, 10),
    }).subscribe((simulation) => {
      this.openSimulateLoanDialog(simulation);
    });
  }

  openCreateClientDialog(): void {
    const dialogRef = this.dialog.open(CreateClientDialog, {
      width: '440px',
      disableClose: true,
      panelClass: 'create-client-dialog-panel',
    });

    dialogRef.afterClosed().subscribe((client) => {
      if (client) {
        this.snackBar.open('Cliente criado com sucesso.', 'Fechar', {
          duration: 3000,
        });
      }
    });
  }

  openSimulateLoanDialog(loan: SimulatedLoan): void {
    this.dialog.open(SimulateLoanDialog, {
      width: '440px',
      disableClose: true,
      panelClass: 'create-client-dialog-panel',
      data: loan,
    });
  }
}
