import { CommonModule } from '@angular/common';
import { Component, computed } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ThemeService } from '../../core/services/theme.service';

@Component({
  selector: 'app-settings',
  imports: [CommonModule, MatSlideToggleModule],
  templateUrl: './settings.html',
  styleUrl: './settings.scss'
})
export class Settings {
  isDark = computed(() => this.theme.isDarkMode());
  constructor(private theme: ThemeService) {}

  toggleTheme(): void {
    this.theme.toggleTheme();
  }
}
