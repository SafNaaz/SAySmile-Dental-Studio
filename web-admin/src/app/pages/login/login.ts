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
    fullName: '',
    userType: 'STAFF'
  };

  isSignupMode = false;

  authService = inject(AuthService);
  errorMessage = '';

  onSubmit() {
    this.errorMessage = '';
    const action = this.isSignupMode 
      ? this.authService.signup(this.loginData) 
      : this.authService.login(this.loginData);
      
    action.subscribe({
      next: () => {
        // success is handled in service where it navigates
      },
      error: (err) => {
        this.errorMessage = this.isSignupMode ? 'Could not create account. Username might be taken.' : 'Invalid username or password.';
        console.error(err);
      }
    });
  }

  toggleMode() {
    this.isSignupMode = !this.isSignupMode;
    this.errorMessage = '';
  }

  setRole(role: string) {
    this.loginData.userType = role;
    if (role === 'STAFF') {
      this.isSignupMode = false;
    }
  }
}
