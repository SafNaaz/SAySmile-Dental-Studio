import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  loginData = {
    username: '',
    password: '',
    userType: 'STAFF'
  };

  authService = inject(AuthService);
  errorMessage = '';

  onSubmit() {
    this.errorMessage = '';
    this.authService.login(this.loginData).subscribe({
      next: () => {
        // success is handled in service where it navigates
      },
      error: (err) => {
        this.errorMessage = 'Invalid username or password.';
        console.error(err);
      }
    });
  }

  setRole(role: string) {
    this.loginData.userType = role;
  }
}
