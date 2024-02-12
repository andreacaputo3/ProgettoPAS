import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {environment} from "../../enviroments/enviroments";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  registerData = {
    username: '',
    password: '',
    mail: '',
    name: '',
    surname: '',
    age: null
  };
  errorMessage: string = '';
  successMessage: string = '';
  loading: boolean = false;

  constructor(private router: Router, private http: HttpClient) {}

  registerUser(): void {
    // Controllo se tutti i campi sono stati compilati
    if (!this.registerData.username || !this.registerData.password || !this.registerData.mail ||
      !this.registerData.name || !this.registerData.surname || !this.registerData.age) {
      this.errorMessage = 'Tutti i campi sono obbligatori.';
      return;
    }
    if (this.registerData.age <= 0) {
      this.errorMessage = 'L\'età deve essere maggiore di 0.';
      return;
    }

    //effettiva registrazione
    this.loading = true;
    let url = `${environment.apiUrl}/users/register`;

    this.http.post<any>(url, this.registerData).subscribe({
      next: (response) => {
        // Registrazione avvenuta con successo
        this.successMessage = 'Registrazione avvenuta con successo!';
        this.errorMessage = '';
        this.loading = false;
        localStorage.setItem('jwtToken', response.token);
        localStorage.setItem('id', response.id);
        localStorage.setItem('username', response.username);

        this.router.navigate(['/user-dashboard']);
      },
      error: (error) => {
        // Registrazione fallita
        if (error.status === 400) {
          // Errore di validazione dei dati inviati
          this.errorMessage = error.error;
        } else {
          // Altro errore
          this.errorMessage = 'Errore durante la registrazione. Si prega di riprovare.';
        }
        this.successMessage = '';
        this.loading = false;
      }
    });
  }
}
