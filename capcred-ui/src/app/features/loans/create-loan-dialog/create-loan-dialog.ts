import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { LoansService, CreateLoanInput, Loan } from '../loans.service';
import { ClientsService, Client } from '../../clients/clients.service';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';

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
  ],
  templateUrl: './create-loan-dialog.html',
  styleUrl: './create-loan-dialog.scss',
})
export class CreateLoanDialog {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<CreateLoanDialog>);
  private loansService = inject(LoansService);
  private clientsService = inject(ClientsService);
  private initialData = inject(MAT_DIALOG_DATA, { optional: true }) as Partial<Loan> | null;

  submitting = signal(false);
  errorMessage = signal<string | null>(null);

  readonly clients = toSignal(this.clientsService.getClients(), { initialValue: [] as Client[] });

  form = this.fb.nonNullable.group({
    clientId: ['', Validators.required],
    amount: [10000, [Validators.required, Validators.min(100)]],
    installments: [12, [Validators.required, Validators.min(1)]],
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

  constructor() {
    const data = this.initialData;
    if (data?.clientId) {
      this.form.patchValue({ clientId: data.clientId });
    }

    this.form.controls.amount.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe((value) => {
        this.amountSignal.set(Number(value) || 0);
      });

    this.form.controls.installments.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe((value) => {
        this.installmentsSignal.set(Math.max(Number(value) || 0, 0));
      });
  }

  submit(): void {
    if (this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    const { clientId, amount, installments } = this.form.getRawValue();
    const clients = this.clients();
    const selected = clients.find((c) => c.id === clientId);

    if (!selected) {
      this.errorMessage.set('Selecione um cliente válido.');
      return;
    }

    const payload: CreateLoanInput = {
      clientId: selected.id,
      clientName: selected.name,
      clientEmail: selected.email,
      amount: Number(amount),
      installments: Number(installments),
    };

    this.submitting.set(true);
    this.errorMessage.set(null);

    this.loansService.createLoan(payload).subscribe({
      next: (loan) => this.dialogRef.close(loan),
      error: (err) => {
        const message = err?.message ?? 'Não foi possível criar o empréstimo.';
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
}
