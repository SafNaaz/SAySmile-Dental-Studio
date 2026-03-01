import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const user = localStorage.getItem('saysmile_user');

  if (user) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
