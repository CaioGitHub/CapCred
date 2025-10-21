import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { PAYMENTS_MOCK } from '../../core/mocks/payments.mock';

@Component({
  selector: 'app-payments',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatTableModule, MatButtonModule],
  templateUrl: './payments.html',
  styleUrl: './payments.scss'
})
export class Payments {
  cols = ['id', 'client', 'value', 'dueDate', 'status', 'actions'];
  payments = PAYMENTS_MOCK;
}
