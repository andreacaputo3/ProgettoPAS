import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {environment} from "../../enviroments/enviroments";


@Component({
  selector: 'app-login',
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.scss'],
})
export class LoginComponent {
  loginData = {
    username: '', // Assicurati che 'username' sia il nome corretto del campo utilizzato dal backend
    password: ''
  };
  error = '';

  constructor(private router: Router, private http: HttpClient) {}

  login() {
    let url = `${environment.apiUrl}/users/login`;
    this.http.post<any>(url, this.loginData).subscribe({
      next: (response) => {
        localStorage.setItem('jwtToken', response.token);
        localStorage.setItem('username', this.loginData.username);
        localStorage.setItem('id', response.id);

        // Esegui il reindirizzamento in base al ruolo
        switch (response.role) {
          case 'ADMIN':
            this.router.navigate(['/admin-dashboard']);
            break;
          case 'ADMIN_UFFICIO':
            this.router.navigate(['/office-admin-dashboard']);
            break;
          case 'ADMIN_AZIENDA':
            this.router.navigate(['/company-admin-dashboard']);
            break;
          case 'USER':
            this.router.navigate(['/user-dashboard']);
            break;
          default:
            console.error('Ruolo non gestito:', response.role);
            // Reindirizza a una pagina predefinita o gestisci il caso in modo appropriato
            break;
        }
      },
      error: (error) => {
        // Gestisci l'errore e visualizza un messaggio appropriato all'utente
        console.error('Errore durante il login:', error);
        // Mostra un messaggio di errore all'utente
        this.error = 'Credenziali non valide. Riprova.';
      }
    });
  }
}
