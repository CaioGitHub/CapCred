import { Component, inject, ViewChild } from '@angular/core';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../../components/sidebar/sidebar';
import { Header } from '../../components/header/header';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { LoadingComponent } from '../../../core/shared/components/loading/loading.component';
import { LoadingService } from '../../../core/shared/services/loading.service';
import { fadeAnimation } from '../../../core/animations/route-animations';

@Component({
  selector: 'app-shell-layout',
  standalone: true,
  imports: [
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    RouterOutlet,
    Sidebar,
    Header,
    LoadingComponent
  ],
  templateUrl: './shell-layout.html',
  styleUrls: ['./shell-layout.scss'],
  animations: [fadeAnimation],
})
export class ShellLayout {
  @ViewChild(MatSidenav) sidenav!: MatSidenav;
  isMobile = false;
  loading = inject(LoadingService);

  constructor(private observer: BreakpointObserver) {}

  ngAfterViewInit() {
    this.observer.observe([
      Breakpoints.Medium,
      Breakpoints.Small,
      Breakpoints.Handset
    ]).subscribe(result => {
      this.isMobile = result.matches;
      if (this.isMobile) {
        this.sidenav.mode = 'over';
        this.sidenav.close();
      } else {
        this.sidenav.mode = 'side';
        this.sidenav.open();
      }
    });
  }


  toggleSidebar() {
    this.sidenav.toggle();
  }
}
