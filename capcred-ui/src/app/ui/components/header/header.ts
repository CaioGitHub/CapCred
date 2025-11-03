import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, computed } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ThemeService } from '../../../core/services/theme.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CreateLoanDialog } from '../../../features/loans/create-loan-dialog/create-loan-dialog';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    CommonModule,
    MatDialogModule,
    MatSnackBarModule,
  ],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class Header {
  @Input() isMobile = false;
  @Output() menuClicked = new EventEmitter<void>();

  isDark = computed(() => this.theme.isDarkMode());
  themeIcon = computed(() => (this.isDark() ? 'light_mode' : 'dark_mode'));
  themeLabel = computed(() => (this.isDark() ? 'Ativar modo claro' : 'Ativar modo escuro'));

  constructor(
    private theme: ThemeService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  onMenuClick() {
    this.menuClicked.emit();
  }

  toggleTheme(): void {
    this.theme.toggleTheme();
  }

  openCreateLoanDialog(): void {
    const dialogRef = this.dialog.open(CreateLoanDialog, {
      width: '440px',
      disableClose: true,
      panelClass: 'create-client-dialog-panel',
    });

    dialogRef.afterClosed().subscribe((loan) => {
      if (loan) {
        this.snackBar.open('Empréstimo criado com sucesso.', 'Fechar', {
          duration: 3000,
        });
      }
    });
  }
}
