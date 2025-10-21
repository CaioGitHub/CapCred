import { Component } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loans-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule],
  template: `
    <div class="page-header">
      <h2>Empréstimos</h2>
      <button mat-flat-button color="primary">
        <mat-icon>add</mat-icon> Novo Empréstimo
      </button>
    </div>

    <table mat-table [dataSource]="loans" class="mat-elevation-z0 full-table">
      <ng-container matColumnDef="client">
        <th mat-header-cell *matHeaderCellDef>Cliente</th>
        <td mat-cell *matCellDef="let l">{{ l.client }}</td>
      </ng-container>

      <ng-container matColumnDef="amount">
        <th mat-header-cell *matHeaderCellDef>Valor</th>
        <td mat-cell *matCellDef="let l">R$ {{ l.amount | number }}</td>
      </ng-container>

      <ng-container matColumnDef="status">
        <th mat-header-cell *matHeaderCellDef>Status</th>
        <td mat-cell *matCellDef="let l">
          <span [ngClass]="l.status.toLowerCase()" class="status">{{ l.status }}</span>
        </td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef>Ações</th>
        <td mat-cell *matCellDef="let l">
          <button mat-icon-button color="primary"><mat-icon>visibility</mat-icon></button>
          <button mat-icon-button color="accent"><mat-icon>edit</mat-icon></button>
          <button mat-icon-button color="warn"><mat-icon>delete</mat-icon></button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="cols"></tr>
      <tr mat-row *matRowDef="let row; columns: cols;"></tr>
    </table>
  `,
  styles: [
    `
      .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1rem;
      }
      .full-table {
        width: 100%;
      }
      .status {
        padding: 2px 8px;
        border-radius: 8px;
        color: white;
        font-size: 0.8rem;
      }
      .status.aprovado {
        background: #4caf50;
      }
      .status.pendente {
        background: #ff9800;
      }
      .status.rejeitado {
        background: #f44336;
      }
    `,
  ],
})
export class LoansListComponent {
  cols = ['client', 'amount', 'status', 'actions'];
  loans = [
    { client: 'Maria Santos', amount: 15000, status: 'Aprovado' },
    { client: 'Carlos Lima', amount: 8500, status: 'Pendente' },
    { client: 'Ana Costa', amount: 25000, status: 'Rejeitado' },
  ];
}
 