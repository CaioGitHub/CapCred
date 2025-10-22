import { animate, style, transition, trigger } from '@angular/animations';

export const fadeAnimation = trigger('routeFadeAnimation', [
  transition(':enter', [
    style({ opacity: 0, transform: 'translateY(10px)' }),
    animate('350ms ease-out', style({ opacity: 1, transform: 'translateY(0)' })),
  ]),
  transition(':leave', [
    style({ opacity: 1 }),
    animate('250ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' })),
  ]),
]);
