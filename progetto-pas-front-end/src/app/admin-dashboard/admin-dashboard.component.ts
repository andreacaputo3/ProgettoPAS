// admin-dashboard.component.ts
import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {environment} from "../../enviroments/enviroments";

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

  constructor(private http: HttpClient) {}

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
        return; // Esce dalla funzione in caso di ruolo non gestito
    }

    this.http.post<any>(url, this.userData, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          console.log('Nuovo utente creato:', response);
          // Aggiorna l'interfaccia utente in base alla risposta del backend
        },
        error: (error) => {
          console.error('Errore durante la creazione dell\'utente:', error);
          // Gestisci l'errore e aggiorna l'interfaccia utente
        }
      });
  }

  private getHeaders(): HttpHeaders {
    let headers = new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('jwtToken')}`);
    headers = headers.append("Content-Type", "application/json");
    return headers;
  }

}
