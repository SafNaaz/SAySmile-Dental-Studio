import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-patients',
  imports: [CommonModule],
  templateUrl: './patients.html',
  styleUrl: './patients.css'
})
export class Patients implements OnInit {
  http = inject(HttpClient);
  patients: any[] = [];

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/api/patients').subscribe({
      next: (data) => this.patients = data,
      error: (err) => console.error('Error fetching patients', err)
    });
  }
}
