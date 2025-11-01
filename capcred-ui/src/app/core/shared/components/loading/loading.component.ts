import { Component, Input } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [MatProgressSpinnerModule, NgIf],
  template: `
    <div class="loading-overlay" *ngIf="show">
      <mat-progress-spinner
        diameter="60"
        mode="indeterminate"
        color="primary"
      ></mat-progress-spinner>
    </div>
  `,
  styles: [
    `
      .loading-overlay {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        background: rgba(255, 255, 255, 0.7);
        z-index: 2000;
        backdrop-filter: blur(2px);
      }
    `,
  ],
})
export class LoadingComponent {
  @Input() show = false;
}
