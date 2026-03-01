import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-billing',
  imports: [CommonModule],
  templateUrl: './billing.html',
  styleUrl: './billing.css'
})
export class Billing implements OnInit {
  http = inject(HttpClient);
  invoices: any[] = [];

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/api/invoices').subscribe({
      next: (data) => this.invoices = data,
      error: (err) => console.error('Error fetching invoices', err)
    });
  }
}
