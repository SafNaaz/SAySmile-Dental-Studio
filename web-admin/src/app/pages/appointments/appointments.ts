import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-appointments',
  imports: [CommonModule, FormsModule],
  templateUrl: './appointments.html',
  styleUrl: './appointments.css'
})
export class Appointments implements OnInit {
  http = inject(HttpClient);
  authService = inject(AuthService);
  appointments: any[] = [];
  patients: any[] = [];
  showForm = false;
  
  newAppointment: any = {
    procedureType: '',
    appointmentTime: '',
    notes: '',
    patient: { id: null },
    doctor: { id: 1 } // Default Dr. Aysha
  };

  ngOnInit(): void {
    this.loadAppointments();
    if (!this.authService.hasAnyRole(['PATIENT'])) {
      this.loadPatients();
    }
  }

  loadPatients() {
    this.http.get<any[]>('http://localhost:8080/api/patients').subscribe({
      next: (data) => this.patients = data,
      error: (err) => console.error('Error fetching patients', err)
    });
  }

  loadAppointments() {
    this.http.get<any[]>('http://localhost:8080/api/appointments').subscribe({
      next: (data) => this.appointments = data,
      error: (err) => console.error('Error fetching appointments', err)
    });
  }

  toggleForm() {
    this.showForm = !this.showForm;
  }

  bookAppointment() {
    this.http.post('http://localhost:8080/api/appointments', this.newAppointment).subscribe({
      next: () => {
        this.loadAppointments();
        this.showForm = false;
        this.newAppointment = { procedureType: '', appointmentTime: '', notes: '', patient: { id: null }, doctor: { id: 1 } };
      },
      error: (err) => console.error('Error booking appointment', err)
    });
  }
}
