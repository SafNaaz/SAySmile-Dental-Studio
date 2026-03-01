import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-billing',
  imports: [CommonModule, FormsModule],
  templateUrl: './billing.html',
  styleUrl: './billing.css'
})
export class Billing implements OnInit {
  http = inject(HttpClient);
  invoices: any[] = [];
  showForm = false;
  
  newInvoice = {
    patientId: null,
    appointmentId: null,
    subTotal: 0,
    cgst: 0,
    sgst: 0,
    totalAmount: 0,
    paymentStatus: 'PENDING',
    paymentMethod: 'UPI'
  };

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices() {
    this.http.get<any[]>(`${environment.apiBaseUrl}/api/invoices`).subscribe({
      next: (data) => this.invoices = data,
      error: (err) => console.error('Error fetching invoices', err)
    });
  }

  toggleForm() {
    this.showForm = !this.showForm;
  }

  createInvoice() {
    // Basic calculation
    this.newInvoice.totalAmount = this.newInvoice.subTotal + this.newInvoice.cgst + this.newInvoice.sgst;
    
    this.http.post(`${environment.apiBaseUrl}/api/invoices`, this.newInvoice).subscribe({
      next: () => {
        this.loadInvoices();
        this.showForm = false;
        this.newInvoice = { patientId: null, appointmentId: null, subTotal: 0, cgst: 0, sgst: 0, totalAmount: 0, paymentStatus: 'PENDING', paymentMethod: 'UPI' };
      },
      error: (err) => console.error('Error creating invoice', err)
    });
  }
}
