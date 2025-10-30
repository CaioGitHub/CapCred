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
      error: (err: Error) => {
        this.loading = false;
        this.error = err.message || 'Nao foi possivel completar o cadastro.';
      },
    });
  }

  goToLogin(): void {
    if (this.loading) {
      return;
    }
    this.router.navigate(['/login']);
  }
}
