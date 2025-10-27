import { DOCUMENT } from '@angular/common';
import { Injectable, inject, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly document = inject(DOCUMENT);
  private readonly storageKey = 'theme';
  private readonly darkMode = signal<boolean>(false);

  constructor() {
    const stored = this.getStoredPreference();
    if (stored) {
      this.darkMode.set(stored === 'dark');
    } else {
      this.darkMode.set(this.prefersDarkMode());
    }

    this.applyTheme();
    this.listenToSystemChanges(Boolean(stored));
  }

  isDarkMode = this.darkMode.asReadonly();

  toggleTheme(): void {
    this.setTheme(!this.darkMode());
  }

  private applyTheme(): void {
    const html = this.document?.documentElement;
    if (!html) {
      return;
    }
    if (this.darkMode()) {
      html.classList.add('dark-theme');
    } else {
      html.classList.remove('dark-theme');
    }
  }

  private setTheme(isDark: boolean): void {
    this.darkMode.set(isDark);
    this.persistPreference(isDark);
    this.applyTheme();
  }

  private getStoredPreference(): 'light' | 'dark' | null {
    if (typeof window === 'undefined') {
      return null;
    }
    const stored = window.localStorage.getItem(this.storageKey);
    if (stored === 'light' || stored === 'dark') {
      return stored;
    }
    return null;
  }

  private persistPreference(isDark: boolean): void {
    if (typeof window === 'undefined') {
      return;
    }
    window.localStorage.setItem(this.storageKey, isDark ? 'dark' : 'light');
  }

  private prefersDarkMode(): boolean {
    if (typeof window === 'undefined' || !window.matchMedia) {
      return false;
    }
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
  }

  private listenToSystemChanges(hasStoredPreference: boolean): void {
    if (typeof window === 'undefined' || hasStoredPreference || !window.matchMedia) {
      return;
    }

    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    mediaQuery.addEventListener('change', (event) => {
      this.setTheme(event.matches);
    });
  }
}
