import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { LOANS_MOCK } from '../../core/mocks/loans.mock';

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule],
  templateUrl: './loans.html',
  styleUrls: ['./loans.scss']
})
export class Loans {
  cols = ['client', 'amount', 'status', 'actions'];
  loans = LOANS_MOCK;
}
