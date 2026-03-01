import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { environment } from '../../../environments/environment';

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

  editMode = false;
  editAppointmentId: number | null = null;

  ngOnInit(): void {
    this.loadAppointments();
    if (!this.authService.hasAnyRole(['PATIENT'])) {
      this.loadPatients();
    }
  }

  loadPatients() {
    this.http.get<any[]>(`${environment.apiBaseUrl}/api/patients`).subscribe({
      next: (data) => this.patients = data,
      error: (err) => console.error('Error fetching patients', err)
    });
  }

  loadAppointments() {
    this.http.get<any[]>(`${environment.apiBaseUrl}/api/appointments`).subscribe({
      next: (data) => this.appointments = data,
      error: (err) => console.error('Error fetching appointments', err)
    });
  }

  toggleForm() {
    this.showForm = !this.showForm;
  }

  bookAppointment() {
    if (this.editMode && this.editAppointmentId) {
      this.http.put(`${environment.apiBaseUrl}/api/appointments/${this.editAppointmentId}`, this.newAppointment).subscribe({
        next: () => {
          this.loadAppointments();
          this.resetForm();
        },
        error: (err) => console.error('Error updating appointment', err)
      });
    } else {
      this.http.post(`${environment.apiBaseUrl}/api/appointments`, this.newAppointment).subscribe({
        next: () => {
          this.loadAppointments();
          this.resetForm();
        },
        error: (err) => console.error('Error booking appointment', err)
      });
    }
  }

  editAppointment(appt: any) {
    this.editMode = true;
    this.editAppointmentId = appt.id;
    this.showForm = true;
    this.newAppointment = {
      procedureType: appt.procedureType,
      appointmentTime: appt.appointmentTime,
      notes: appt.notes,
      status: appt.status, // Preserve current status
      patient: { id: appt.patient?.id },
      doctor: { id: appt.doctor?.id }
    };
  }

  cancelAppointment(id: number) {
    if (confirm('Are you sure you want to cancel this appointment?')) {
      this.http.delete(`${environment.apiBaseUrl}/api/appointments/${id}`).subscribe({
        next: () => this.loadAppointments(),
        error: (err) => console.error('Error cancelling appointment', err)
      });
    }
  }

  resetForm() {
    this.showForm = false;
    this.editMode = false;
    this.editAppointmentId = null;
    this.newAppointment = { procedureType: '', appointmentTime: '', notes: '', patient: { id: null }, doctor: { id: 1 } };
  }
}
