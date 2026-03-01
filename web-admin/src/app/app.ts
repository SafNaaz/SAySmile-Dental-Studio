import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  title = 'SAySmile Dental Studio Dashboard';
  http = inject(HttpClient);
  
  dashboardData: any = null;

  ngOnInit(): void {
    this.http.get('http://localhost:8080/api/analytics/dashboard').subscribe({
      next: (data) => {
        this.dashboardData = data;
        console.log('Dashboard Data:', data);
      },
      error: (err) => {
        console.error('Error fetching dashboard data', err);
      }
    });
  }
}
