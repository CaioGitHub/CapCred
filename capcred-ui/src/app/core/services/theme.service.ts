import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private darkMode = signal<boolean>(false);

  constructor() {
    const stored = localStorage.getItem('theme');
    this.darkMode.set(stored === 'dark');
    this.applyTheme();
  }

  isDarkMode = this.darkMode.asReadonly();

  toggleTheme(): void {
    this.darkMode.update((d) => !d);
    localStorage.setItem('theme', this.darkMode() ? 'dark' : 'light');
    this.applyTheme();
  }

  private applyTheme(): void {
    const html = document.documentElement;
    if (this.darkMode()) {
      html.classList.add('dark-theme');
    } else {
      html.classList.remove('dark-theme');
    }
  }
}
