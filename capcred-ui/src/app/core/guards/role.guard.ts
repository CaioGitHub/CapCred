import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);

  const requiredRoles = route.data['roles'] as string[] | undefined;
  const currentUser = authService.currentUserValue;

  // Se não há roles definidas, permite acesso
  if (!requiredRoles || requiredRoles.length === 0) {
    return true;
  }

  // Verifica se usuário tem uma das roles necessárias
  if (currentUser && requiredRoles.includes(currentUser.role)) {
    return true; // ✅ Tem permissão
  }

  // ❌ Sem permissão
  snackBar.open('Você não tem permissão para acessar esta página.', 'Fechar', {
    duration: 4000,
    panelClass: ['snackbar-error'],
  });

  router.navigate(['/dashboard']); // Redireciona para dashboard
  return false;
};
