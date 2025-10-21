import { Component } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clients-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule],
  template: `
    <div class="page-header">
      <h2>Clientes</h2>
      <button mat-flat-button color="primary">
        <mat-icon>person_add</mat-icon>
        Novo Cliente
      </button>
    </div>

    <table mat-table [dataSource]="clients" class="mat-elevation-z0 full-table">
      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef>Nome</th>
        <td mat-cell *matCellDef="let c">{{ c.name }}</td>
      </ng-container>

      <ng-container matColumnDef="email">
        <th mat-header-cell *matHeaderCellDef>Email</th>
        <td mat-cell *matCellDef="let c">{{ c.email }}</td>
      </ng-container>

      <ng-container matColumnDef="phone">
        <th mat-header-cell *matHeaderCellDef>Telefone</th>
        <td mat-cell *matCellDef="let c">{{ c.phone }}</td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef>Ações</th>
        <td mat-cell *matCellDef="let c">
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

      th.mat-header-cell {
        font-weight: 600;
      }
    `,
  ],
})
export class ClientsListComponent {
  cols = ['name', 'email', 'phone', 'actions'];
  clients = [
    { name: 'Maria Santos', email: 'maria@email.com', phone: '(11) 98877-2211' },
    { name: 'Carlos Lima', email: 'carlos@email.com', phone: '(21) 99912-4567' },
    { name: 'Ana Costa', email: 'ana@email.com', phone: '(31) 97777-3333' },
  ];
}
