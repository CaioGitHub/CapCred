import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { LOANS_MOCK } from '../../core/mocks/loans.mock';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MockDataService } from '../../core/services/mock-data.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

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

  constructor(private mockData: MockDataService) {}

  ngOnInit() {
    this.mockData.getLoans().subscribe((data) => {
      this.dataSource.data = data;
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
