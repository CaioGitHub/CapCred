import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { ClientsService, Client } from '../../clients/clients.service';
import { CreateLoanInput, Loan, LoanStatus, LoansService } from '../loans.service';
import { AuthService, User } from '../../../core/services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-create-loan-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './create-loan-dialog.html',
  styleUrl: './create-loan-dialog.scss',
})
export class CreateLoanDialog {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<CreateLoanDialog>);
  private loansService = inject(LoansService);
  private clientsService = inject(ClientsService);
  private injectedData = inject(MAT_DIALOG_DATA, { optional: true }) as Partial<Loan> | null;

  readonly editingLoan = this.injectedData ?? null;
  readonly isEditMode = !!this.editingLoan;
  readonly editingClientId = this.editingLoan
    ? this.editingLoan.clientId ?? this.editingLoan.email ?? this.editingLoan.client ?? `loan-${this.editingLoan.id}`
    : '';
  readonly editingClientName = this.editingLoan?.client ?? '';
  readonly editingClientEmail = this.editingLoan?.email ?? '';

  readonly needsFallbackClientOption = computed(() => {
    if (!this.isEditMode || !this.editingClientId) {
      return false;
    }
    return !this.clients().some((client) => client.id === this.editingClientId);
  });

  submitting = signal(false);
  errorMessage = signal<string | null>(null);

  readonly clients = toSignal(this.clientsService.getClients(), { initialValue: [] as Client[] });

  private defaultDueDate = this.addDays(new Date(), 30);

  form = this.fb.nonNullable.group({
    clientId: ['', Validators.required],
    amount: [10000, [Validators.required, Validators.min(100)]],
    installments: [12, [Validators.required, Validators.min(1)]],
    firstDueDate: [this.defaultDueDate, Validators.required],
  });

  private amountSignal = signal<number>(10000);
  private installmentsSignal = signal<number>(12);

  installmentValue = computed(() => {
    const installments = this.installmentsSignal();
    if (!installments) {
      return 0;
    }
    return this.amountSignal() / installments;
  });

  totalValue = computed(() => this.amountSignal());

  user$: Observable<User | null>;

  constructor(private auth: AuthService) {
    this.user$ = this.auth.currentUser$;
    this.user$.subscribe((user) => {
      if (!user?.isAdmin) {
        this.form.controls.clientId.setValue(user?.id!);
      }
    });
    if (this.isEditMode && this.editingLoan) {
      const editAmount = this.editingLoan.amount ?? 0;
      const editInstallments = this.editingLoan.installments ?? 1;

      this.form.patchValue(
        {
          clientId: this.editingClientId,
          amount: editAmount,
          installments: editInstallments,
          firstDueDate: this.parseDate(this.editingLoan.date),
        },
        { emitEvent: false }
      );
      this.form.controls.clientId.disable({ emitEvent: false });
      this.amountSignal.set(editAmount);
      this.installmentsSignal.set(editInstallments);
    } else {
      this.form.controls.clientId.enable({ emitEvent: false });
      const initialAmount = Number(this.form.controls.amount.value) || 0;
      const initialInstallments = Number(this.form.controls.installments.value) || 1;
      this.amountSignal.set(initialAmount);
      this.installmentsSignal.set(initialInstallments);
      this.form.controls.firstDueDate.setValue(this.defaultDueDate, { emitEvent: false });
    }

    this.form.controls.amount.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe((value) => {
        this.amountSignal.set(Number(value) || 0);
      });

    this.form.controls.installments.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe((value) => {
        const normalized = Number(value) || 0;
        this.installmentsSignal.set(normalized > 0 ? normalized : 0);
      });
  }

  submit(): void {
    if (this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    const { clientId, amount, installments, firstDueDate } = this.form.getRawValue();
    const clients = this.clients();

    const resolvedClientId = this.isEditMode ? this.editingClientId : clientId;
    let clientName = '';
    let clientEmail: string | undefined;

    if (this.isEditMode && this.editingLoan) {
      clientName = this.editingClientName;
      clientEmail = this.editingClientEmail || undefined;
    } else {
      const selected = clients.find((c) => c.id === resolvedClientId);
      if (!selected) {
        this.errorMessage.set('Selecione um cliente valido.');
        return;
      }
      clientName = selected.name;
      clientEmail = selected.email || undefined;
    }

    const normalizedAmount = Number(amount);
    const normalizedInstallments = Number(installments);

    if (normalizedAmount <= 0 || normalizedInstallments <= 0) {
      this.errorMessage.set('Informe valores validos para o emprestimo.');
      return;
    }

    const normalizedDueDate = this.normalizeDate(firstDueDate);
    if (!normalizedDueDate) {
      this.errorMessage.set('Informe uma data de vencimento valida.');
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set(null);

    if (this.isEditMode && this.editingLoan?.id) {
      this.loansService
        .updateLoan(this.editingLoan.id, {
          amount: normalizedAmount,
          installments: normalizedInstallments,
          status: this.editingLoan.status ?? LoanStatus.Pendente,
        })
        .subscribe({
          next: (loan) => this.dialogRef.close(loan),
          error: (err) => {
            const message = err?.message ?? 'Nao foi possivel atualizar o emprestimo.';
            this.errorMessage.set(message);
            this.submitting.set(false);
          },
        });
      return;
    }

    const payload: CreateLoanInput = {
      clientId: resolvedClientId,
      clientName,
      clientEmail,
      amount: normalizedAmount,
      installments: normalizedInstallments,
      firstDueDate: normalizedDueDate,
    };

    this.loansService.createLoan(payload).subscribe({
      next: (loan) => this.dialogRef.close(loan),
      error: (err) => {
        const message = err?.message ?? 'Nao foi possivel criar o emprestimo.';
        this.errorMessage.set(message);
        this.submitting.set(false);
      },
    });
  }

  cancel(): void {
    if (!this.submitting()) {
      this.dialogRef.close();
    }
  }

  private addDays(date: Date, days: number): Date {
    const clone = new Date(date);
    clone.setDate(clone.getDate() + days);
    return clone;
  }

  private parseDate(value: string | null | undefined): Date {
    if (!value) {
      return this.defaultDueDate;
    }
    const parsed = new Date(value);
    return Number.isNaN(parsed.getTime()) ? this.defaultDueDate : parsed;
  }

  private normalizeDate(value: Date | string | null | undefined): string | null {
    if (!value) {
      return null;
    }
    if (value instanceof Date) {
      return value.toISOString().slice(0, 10);
    }
    const parsed = new Date(value);
    return Number.isNaN(parsed.getTime()) ? null : parsed.toISOString().slice(0, 10);
  }
}
