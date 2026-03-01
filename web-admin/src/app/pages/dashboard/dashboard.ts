import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  authService = inject(AuthService);
  http = inject(HttpClient);
  dashboardData: any = null;

  ngOnInit(): void {
    this.http.get(`${environment.apiBaseUrl}/api/analytics/dashboard`).subscribe({
      next: (data) => {
        this.dashboardData = data;
      },
      error: (err) => {
        console.error('Error fetching dashboard data', err);
      }
    });
  }
}
