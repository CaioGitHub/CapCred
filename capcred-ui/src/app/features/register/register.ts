import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { fadeAnimation } from '../../core/animations/route-animations';
import { AuthService, RegisterPayload } from '../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
  ],
  templateUrl: './register.html',
  styleUrls: ['./register.scss'],
  animations: [fadeAnimation],
})
export class Register {
  form = {
    name: '',
    cpf: '',
    email: '',
    password: '',
    monthlyIncome: null as number | null,
  };
  loading = false;
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  onCpfInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);

    const part1 = digits.slice(0, 3);
    const part2 = digits.slice(3, 6);
    const part3 = digits.slice(6, 9);
    const part4 = digits.slice(9, 11);

    const formatted = [
      part1,
      part2 ? `.${part2}` : '',
      part3 ? `.${part3}` : '',
      part4 ? `-${part4}` : '',
    ].join('');

    this.form.cpf = formatted;
    input.value = formatted;
  }

  onSubmit(form: NgForm): void {
    if (this.loading || form.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    const rawIncome = this.form.monthlyIncome;
    if (rawIncome === null) {
      this.loading = false;
      this.error = 'Informe uma renda mensal valida.';
      return;
    }

    const monthlyIncome = Number(rawIncome);
    if (!Number.isFinite(monthlyIncome)) {
      this.loading = false;
      this.error = 'Informe uma renda mensal valida.';
      return;
    }

    const payload: RegisterPayload = {
      name: this.form.name,
      cpf: this.form.cpf,
      email: this.form.email,
      password: this.form.password,
      monthlyIncome,
    };

    this.auth.register(payload).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/login'], {
          state: { registered: true },
        });
      },
      error: (err: unknown) => {
        this.loading = false;
        this.error = this.resolveErrorMessage(err);
      },
    });
  }

  goToLogin(): void {
    if (this.loading) {
      return;
    }
    this.router.navigate(['/login']);
  }

  private resolveErrorMessage(err: unknown): string {
    if (!err) {
      return 'Nao foi possivel completar o cadastro.';
    }

    if (err instanceof Error && err.message) {
      if (!err.message.startsWith('Http failure response')) {
        return err.message;
      }
    }

    if (err instanceof HttpErrorResponse) {
      const serverMessage =
        typeof err.error === 'string'
          ? err.error.trim()
          : err.error?.message || err.error?.detail || err.message;
      console.log('Server message:', serverMessage);
      console.log('Status code:', err.status);
      console.log('Error body:', err.error);
      if (err.status === 409 || err.status === 403) {
        return serverMessage || 'Este email ja esta cadastrado.';
      }

      return serverMessage || 'Nao foi possivel completar o cadastro.';
    }

    return 'Nao foi possivel completar o cadastro.';
  }
}
