import { Component, EventEmitter, HostListener, Input, Output, signal } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class Header {
  @Input() isMobile = false;
  @Output() menuClicked = new EventEmitter<void>();

  onMenuClick() {
    this.menuClicked.emit();
  }
}
