import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { PaymentsService, PaymentRow } from './payments.service';
import { LoadingService } from '../../core/shared/services/loading.service';

@Component({
  selector: 'app-payments',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatPaginator,
    MatSort,
    MatFormFieldModule,
    MatInputModule,
  ],
  templateUrl: './payments.html',
  styleUrl: './payments.scss',
})
export class Payments {
  cols = ['id', 'client', 'value', 'dueDate', 'status', 'actions'];
  dataSource = new MatTableDataSource<PaymentRow>([]);
  filterValue = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private paymentsService: PaymentsService, private loading: LoadingService) {}

  ngOnInit(): void {
    this.loading.show();
    this.paymentsService.getPayments().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.loading.hide();
      },
      error: () => {
        this.loading.hide();
      },
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event): void {
    const value = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.filterValue = value;
    this.dataSource.filter = value;
  }

  clearFilter(): void {
    this.filterValue = '';
    this.dataSource.filter = '';
  }
}
