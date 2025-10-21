import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { CLIENTS_MOCK } from '../../core/mocks/clients.mock';

@Component({
  selector: 'app-clients',
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule],
  templateUrl: './clients.html',
  styleUrl: './clients.scss'
})
export class Clients {
  cols = ['name', 'email', 'phone', 'actions'];
  clients = CLIENTS_MOCK;
}
