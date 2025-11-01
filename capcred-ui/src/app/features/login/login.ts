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

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit() {
    this.loading = true;
    this.error = '';

    this.auth.login(this.email, this.password).subscribe((user) => {
      this.loading = false;
      if (user) {
        this.router.navigate(['/dashboard']);
      } else {
        this.error = 'Credenciais inválidas';
      }
    });
  }
}
