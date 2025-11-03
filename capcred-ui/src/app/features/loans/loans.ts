import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Loan, LoanStatus, LoansService } from './loans.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LoadingService } from '../../core/shared/services/loading.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CreateLoanDialog } from './create-loan-dialog/create-loan-dialog';

@Component({
  selector: 'app-loans',
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
    MatDialogModule,
    MatSnackBarModule,
    MatTooltipModule,
  ],
  templateUrl: './loans.html',
  styleUrls: ['./loans.scss']
})
export class Loans {
  cols = ['client', 'amount', 'installments', 'status', 'actions'];
  dataSource = new MatTableDataSource<Loan>([]);
  filterValue = '';
  loanStatus = LoanStatus;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private loansService: LoansService,
    private loading: LoadingService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.loading.show();
    this.loansService.getLoans().subscribe((data) => {
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

  openEditLoanDialog(loan: Loan) {
    if (loan.status !== this.loanStatus.Pendente && loan.status !== this.loanStatus.Rejeitado) {
      return;
    }

    const dialogRef = this.dialog.open(CreateLoanDialog, {
      width: '440px',
      disableClose: true,
      panelClass: 'create-client-dialog-panel',
      data: loan,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.snackBar.open('Empréstimo atualizado com sucesso.', 'Fechar', {
          duration: 3000,
        });
      }
    });
  }
}
