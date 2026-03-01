import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-inventory',
  imports: [CommonModule],
  templateUrl: './inventory.html',
  styleUrl: './inventory.css'
})
export class Inventory implements OnInit {
  http = inject(HttpClient);
  inventory: any[] = [];

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/api/inventory').subscribe({
      next: (data) => this.inventory = data,
      error: (err) => console.error('Error fetching inventory', err)
    });
  }
}
