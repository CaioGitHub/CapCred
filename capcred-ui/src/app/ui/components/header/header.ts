import { Component, HostListener, signal } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MatToolbarModule, MatButtonModule, MatIconModule],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class Header {
  scrolled = signal(false);

  @HostListener('window:scroll', [])
  onScroll() {
    this.scrolled.set(window.scrollY > 10);
  }
}
