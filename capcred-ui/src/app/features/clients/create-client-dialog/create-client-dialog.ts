import { Component, Inject, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { ClientsService, CreateClientInput } from '../clients.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

export interface CreateClientDialogData {
  presetEmail?: string;
}

@Component({
  selector: 'app-create-client-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
  ],
  templateUrl: './create-client-dialog.html',
  styleUrl: './create-client-dialog.scss',
})
export class CreateClientDialog {
  submitting = signal(false);
  errorMessage = signal<string | null>(null);

  private fb = inject(FormBuilder);

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required, Validators.pattern(/^\(\d{2}\)\d{5}-\d{4}$/)]],
  });

  constructor(
    private dialogRef: MatDialogRef<CreateClientDialog>,
    private clientsService: ClientsService,
    @Inject(MAT_DIALOG_DATA) data: CreateClientDialogData | null
  ) {
    if (data?.presetEmail) {
      this.form.patchValue({ email: data.presetEmail });
    }

    this.form.controls.phone.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe((value) => {
        const formatted = this.formatPhone(value ?? '');
        if (formatted !== value) {
          this.form.controls.phone.setValue(formatted, { emitEvent: false });
        }
      });
  }

  submit(): void {
    if (this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set(null);

    const payload: CreateClientInput = this.form.getRawValue();

    this.clientsService.createClient(payload).subscribe({
      next: (client) => {
        this.dialogRef.close(client);
      },
      error: (err) => {
        const message = err?.message ?? 'Não foi possível criar o cliente.';
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

  private formatPhone(value: string): string {
    const digits = (value || '').replace(/\D/g, '').slice(0, 11);
    if (!digits) {
      return '';
    }

    const ddd = digits.slice(0, 2);
    const middle = digits.slice(2, 7);
    const last = digits.slice(7, 11);

    if (digits.length <= 2) {
      return `(${ddd}`;
    }

    if (digits.length <= 7) {
      return `(${ddd})${middle}`;
    }

    return `(${ddd})${middle}-${last}`;
  }
}
