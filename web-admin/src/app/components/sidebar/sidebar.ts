import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar {
  public authService = inject(AuthService);

  hasRole(roles: string[]): boolean {
    return this.authService.hasAnyRole(roles);
  }

  logout() {
    this.authService.logout();
  }
}
