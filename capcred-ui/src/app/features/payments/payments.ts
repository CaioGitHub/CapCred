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
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';

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
    MatSnackBarModule,
    MatTooltipModule,
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

  constructor(
    private paymentsService: PaymentsService,
    private loading: LoadingService,
    private snackBar: MatSnackBar
  ) {}

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

  /**
   * Processa o pagamento de uma parcela
   * @param payment Linha de pagamento com os dados da parcela
   */
  processPayment(payment: PaymentRow): void {
    // Verifica se já está pago
    if (payment.status.toLowerCase() === 'pago') {
      this.snackBar.open('Esta parcela já foi paga.', 'Fechar', {
        duration: 3000,
        panelClass: ['snackbar-info']
      });
      return;
    }

    this.loading.show();
    this.paymentsService.payInstallment(payment.id).subscribe({
      next: () => {
        this.snackBar.open('Pagamento realizado com sucesso!', 'Fechar', {
          duration: 4000,
          panelClass: ['snackbar-success']
        });
        this.loading.hide();
        // Recarrega a lista de pagamentos
        this.refreshPayments();
      },
      error: (error) => {
        console.error('Erro ao processar pagamento:', error);
        this.snackBar.open(
          'Erro ao processar o pagamento, entre em contato com a instituição financeira.',
          'Fechar',
          {
            duration: 5000,
            panelClass: ['snackbar-error']
          }
        );
        this.loading.hide();
      }
    });
  }

  /**
   * Recarrega a lista de pagamentos
   */
  private refreshPayments(): void {
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
}
