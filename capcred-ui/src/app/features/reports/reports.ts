import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { REPORTS_MOCK } from '../../core/mocks/reports.mock';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatTableModule],
  templateUrl: './reports.html',
  styleUrl: './reports.scss'
})
export class Reports {
  cols = ['name', 'type', 'date'];
  reports = REPORTS_MOCK;
}
