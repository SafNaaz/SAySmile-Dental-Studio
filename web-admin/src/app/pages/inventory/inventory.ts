import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-inventory',
  imports: [CommonModule, FormsModule],
  templateUrl: './inventory.html',
  styleUrl: './inventory.css'
})
export class Inventory implements OnInit {
  http = inject(HttpClient);
  inventory: any[] = [];
  showForm = false;
  newItem = {
    name: '',
    category: '',
    stockQuantity: 0,
    minimumThreshold: 5,
    costPerUnit: 0
  };

  ngOnInit(): void {
    this.loadInventory();
  }

  loadInventory() {
    this.http.get<any[]>(`${environment.apiBaseUrl}/api/inventory`).subscribe({
      next: (data) => this.inventory = data,
      error: (err) => console.error('Error fetching inventory', err)
    });
  }

  toggleForm() {
    this.showForm = !this.showForm;
  }

  addItem() {
    this.http.post(`${environment.apiBaseUrl}/api/inventory`, this.newItem).subscribe({
      next: () => {
        this.loadInventory();
        this.showForm = false;
        this.newItem = { name: '', category: '', stockQuantity: 0, minimumThreshold: 5, costPerUnit: 0 };
      },
      error: (err) => console.error('Error adding item', err)
    });
  }

  deleteItem(id: number) {
    if (confirm('Are you sure you want to delete this item?')) {
      this.http.delete(`${environment.apiBaseUrl}/api/inventory/${id}`).subscribe({
        next: () => this.loadInventory(),
        error: (err) => console.error('Error deleting item', err)
      });
    }
  }
}
