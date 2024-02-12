import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {environment} from "../../enviroments/enviroments";
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent {
  userData = {
    username: '',
    password: '',
    role: ''
  };
  error= '';
  usersData: any;
  loadingUsers = false;

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers(): void {
    this.loadingUsers = true;
    let url = `${environment.apiUrl}/users/`;
    this.http.get<any>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.usersData = response;
        this.loadingUsers = false;
      },
      error: (error) => {
        console.error('Errore durante il recupero degli utenti:', error);
        this.loadingUsers = false;
      }
    });
  }

  deleteUser(userId: string): void {
    if (confirm('Sei sicuro di voler eliminare questo utente?')) {
      let url = `${environment.apiUrl}/users/delete/${userId}`;
      this.http.delete<any>(url, { headers: this.getHeaders() }).subscribe({
        next: (response) => {
          // Aggiorno lista utenti
          this.getAllUsers();

        },
        error: (error) => {
          console.error('Errore durante l\'eliminazione dell\'utente:', error);
          // Gestisci l'errore in base alle tue esigenze
        }
      });
    }
  }

  createUser() {
    let url = `${environment.apiUrl}/admin/`;
    switch (this.userData.role) {
      case 'ADMIN_AZIENDA':
        url = url + 'register-company-admin';
        break;
      case 'ADMIN_UFFICIO':
        url = url + 'register-office-admin';
        break;
      default:
        console.error('Ruolo non gestito:', this.userData.role);
        return;
    }

    this.http.post<any>(url, this.userData, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          this.getAllUsers();
        },
        error: (error) => {
          console.error('Errore durante la creazione dell\'utente:', error);

        }
      });
  }

  private getHeaders(): HttpHeaders {
    let headers = new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('jwtToken')}`);
    headers = headers.append("Content-Type", "application/json");
    return headers;
  }

  logout(): void {
    // Rimuovo credenziali memorizzate nel localStorage
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');

    // Reindirizzo a login
    this.router.navigate(['/login']);
  }

    protected readonly localStorage = localStorage;
}
