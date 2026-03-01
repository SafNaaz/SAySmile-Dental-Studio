import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-appointments',
  imports: [CommonModule],
  templateUrl: './appointments.html',
  styleUrl: './appointments.css'
})
export class Appointments implements OnInit {
  http = inject(HttpClient);
  appointments: any[] = [];

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/api/appointments').subscribe({
      next: (data) => this.appointments = data,
      error: (err) => console.error('Error fetching appointments', err)
    });
  }
}
