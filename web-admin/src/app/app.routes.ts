import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { Patients } from './pages/patients/patients';
import { Appointments } from './pages/appointments/appointments';
import { Inventory } from './pages/inventory/inventory';
import { Billing } from './pages/billing/billing';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: Dashboard },
  { path: 'patients', component: Patients },
  { path: 'appointments', component: Appointments },
  { path: 'inventory', component: Inventory },
  { path: 'billing', component: Billing }
];
