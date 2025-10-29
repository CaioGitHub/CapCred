import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Client, ClientsService } from './clients.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LoadingService } from '../../core/shared/services/loading.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CreateClientDialog } from './create-client-dialog/create-client-dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-clients',
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatPaginator,
    MatSort,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatSnackBarModule,
  ],
  templateUrl: './clients.html',
  styleUrl: './clients.scss'
})
export class Clients {
  cols = ['name', 'email', 'phone', 'actions'];
  dataSource = new MatTableDataSource<Client>([]);
  filterValue = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private clientsService: ClientsService,
    private loading: LoadingService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.loading.show();
    this.clientsService.getClients().subscribe((data) => {
      this.dataSource.data = data;

      this.loading.hide();
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

  openCreateClientDialog() {
    const dialogRef = this.dialog.open(CreateClientDialog, {
      width: '440px',
      disableClose: true,
      panelClass: 'create-client-dialog-panel',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.snackBar.open('Cliente criado com sucesso.', 'Fechar', {
          duration: 3000,
        });
      }
    });
  }
}
