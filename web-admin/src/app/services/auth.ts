import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    const storedUser = localStorage.getItem('saysmile_user');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  login(credentials: any) {
    return this.http.post<any>('http://localhost:8080/api/auth/login', credentials).pipe(
      tap(res => {
        if (res && res.token) {
          localStorage.setItem('saysmile_user', JSON.stringify(res));
          this.currentUserSubject.next(res);
          this.router.navigate(['/dashboard']);
        }
      })
    );
  }

  signup(data: any) {
    return this.http.post<any>('http://localhost:8080/api/auth/signup', data).pipe(
      tap(res => {
        if (res && res.token) {
          localStorage.setItem('saysmile_user', JSON.stringify(res));
          this.currentUserSubject.next(res);
          this.router.navigate(['/dashboard']);
        }
      })
    );
  }

  logout() {
    const user = this.currentUserSubject.value;
    if (user && user.username) {
      this.http.post('http://localhost:8080/api/auth/logout', { username: user.username }).subscribe();
    }
    localStorage.removeItem('saysmile_user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    const user = this.currentUserSubject.value;
    return user ? user.token : null;
  }

  getRole(): string | null {
    const user = this.currentUserSubject.value;
    return user ? user.role : null;
  }

  hasAnyRole(roles: string[]): boolean {
    const currentRole = this.getRole();
    if (!currentRole) return false;
    return roles.includes(currentRole);
  }
}
