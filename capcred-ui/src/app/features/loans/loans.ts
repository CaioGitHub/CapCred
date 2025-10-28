import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { LoansService } from './loans.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LoadingService } from '../../core/shared/services/loading.service';

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatPaginator, MatSort, MatFormFieldModule, MatInputModule],
  templateUrl: './loans.html',
  styleUrls: ['./loans.scss']
})
export class Loans {
  cols = ['client', 'amount', 'status', 'actions'];
  dataSource = new MatTableDataSource<any>([]);
  filterValue = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private loansService: LoansService, private loading: LoadingService) {}

  ngOnInit() {
    this.loading.show();
    this.loansService.getLoans().subscribe((data) => {
      this.dataSource.data = data;
      setTimeout(() => this.loading.hide(), 500);
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.filterValue = value;
    this.dataSource.filter = value;
  }

  clearFilter() {
    this.filterValue = '';
    this.dataSource.filter = '';
  }
}
