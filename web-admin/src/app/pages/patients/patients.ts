import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-patients',
  imports: [CommonModule, FormsModule],
  templateUrl: './patients.html',
  styleUrl: './patients.css'
})
export class Patients implements OnInit {
  http = inject(HttpClient);
  authService = inject(AuthService);
  patients: any[] = [];
  showForm = false;
  
  newPatient = {
    username: '',
    password: 'password123', // Default for staff-added users
    fullName: '',
    gender: 'Other',
    dateOfBirth: ''
  };

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients() {
    this.http.get<any[]>('http://localhost:8080/api/patients').subscribe({
      next: (data) => this.patients = data,
      error: (err) => console.error('Error fetching patients', err)
    });
  }

  toggleForm() {
    this.showForm = !this.showForm;
  }

  addPatient() {
    // We send this to signup endpoint since it handles creating both User and Patient entities
    this.http.post('http://localhost:8080/api/auth/signup', this.newPatient).subscribe({
      next: () => {
        this.loadPatients();
        this.showForm = false;
        this.newPatient = { username: '', password: 'password123', fullName: '', gender: 'Other', dateOfBirth: '' };
      },
      error: (err) => console.error('Error adding patient', err)
    });
  }

  deletePatient(id: number) {
    if (confirm('Are you sure you want to delete this patient record?')) {
      this.http.delete(`http://localhost:8080/api/patients/${id}`).subscribe({
        next: () => this.loadPatients(),
        error: (err) => console.error('Error deleting patient', err)
      });
    }
  }
}
