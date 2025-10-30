import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { fadeAnimation } from '../../core/animations/route-animations';

@Component({
  selector: 'app-login',
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
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
  animations: [fadeAnimation],
})
export class Login {
  email = '';
  password = '';
  loading = false;
  error = '';
  info = '';

  constructor(private auth: AuthService, private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    const justRegistered = navigation?.extras?.state?.['registered'];
    if (justRegistered) {
      this.info = 'Cadastro realizado com sucesso! Entre com suas credenciais.';
    }
  }

  goToRegister(): void {
    if (this.loading) {
      return;
    }
    this.router.navigate(['/register']);
  }

  onSubmit(): void {
    if (this.loading) {
      return;
    }

    this.loading = true;
    this.error = '';

    this.auth.login(this.email, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err: Error) => {
        this.loading = false;
        this.error = err.message || 'Nao foi possivel realizar o login.';
      },
    });
  }
}
