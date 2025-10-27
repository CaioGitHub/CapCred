import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, computed } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ThemeService } from '../../../core/services/theme.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    CommonModule,
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

  constructor(private theme: ThemeService) {}

  onMenuClick() {
    this.menuClicked.emit();
  }

  toggleTheme(): void {
    this.theme.toggleTheme();
  }
}
