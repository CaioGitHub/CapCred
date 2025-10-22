import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MockDataService } from '../../core/services/mock-data.service';

@Component({
  selector: 'app-clients',
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatPaginator, MatSort],
  templateUrl: './clients.html',
  styleUrl: './clients.scss'
})
export class Clients {
  cols = ['name', 'email', 'phone', 'actions'];
  dataSource = new MatTableDataSource<any>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private mockData: MockDataService) {}

  ngOnInit() {
    this.mockData.getClients().subscribe((data) => {
      this.dataSource.data = data;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}
