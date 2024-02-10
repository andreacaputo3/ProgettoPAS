import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import {environment} from "../../enviroments/enviroments";

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss']
})
export class UserDashboardComponent {
  paymentsData: any;
  wasteSeparationPerformanceData: any;
  noPaymentsMessage: string = '';
  locations: any[] | undefined;
  errorMessage = '';
  responseMessage = '';
  disposalData = {
    binId: '',
    userId: localStorage.getItem("id"),
    wasteType: '',
    weight: ''
  };

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    this.getPaymentsByUserId();
    this.getUserWasteSeparationPerformance();
    this.getBinLocations();
  }

  getBinLocations(): void {
    let url = `${environment.apiUrl}/bins/`;
    this.http.get<any[]>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.locations = response;
      },
      error: (error) => {
        console.error('Errore durante il recupero delle locazioni dei cassonetti:', error);
      }
    });
  }

  submitDisposal(): void {
    if (!this.disposalData || !this.disposalData.binId || !this.disposalData.wasteType || !this.disposalData.weight) {
      console.error('Compilare tutti i campi prima di conferire il rifiuto.');
      this.errorMessage = "Compilare tutti i campi prima di conferire il rifiuto.";
      return;
    }

    let userId = localStorage.getItem("id");
    let url = `${environment.apiUrl}/waste-disposals/create`;

    this.http.post<any>(url, this.disposalData, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.errorMessage = '';
        this.responseMessage = 'Conferimento eseguito con successo';
      },
      error: (error) => {
        console.error('Errore durante il conferimento del rifiuto:', error);
        this.errorMessage = 'Non puoi inserire questo conferimento, cassonetto pieno';
      }
    });

  }

  getPaymentsByUserId(): void {
    let id = localStorage.getItem("id");
    let url = `${environment.apiUrl}/users/${id}/payments`;
    this.http.get<any>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.paymentsData = response;
      },
      error: (error) => {
        console.error('Errore durante il recupero dei pagamenti:', error);
        if (error.status === 404) {
          this.noPaymentsMessage = 'Nessun pagamento disponibile';
        }
      }
    });
  }

  payPayment(paymentId: string): void {
    let userId = localStorage.getItem("id");
    let url = `${environment.apiUrl}/users/${userId}/payments/${paymentId}/pay`;
    this.http.post<any>(url, null, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.getPaymentsByUserId();
      },
      error: (error) => {
        console.error('Errore durante il pagamento:', error);
      }
    });
  }

  getUserWasteSeparationPerformance(): void {
    let id = localStorage.getItem("id");
    let url = `${environment.apiUrl}/users/${id}/waste-separation-performance`;
    this.http.get<any>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.wasteSeparationPerformanceData = response;
      },
      error: (error) => {
        console.error('Errore durante il recupero delle prestazioni di separazione dei rifiuti:', error);
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

    // Reindirizzo utente al login
    this.router.navigate(['/login']);
  }

  protected readonly Object = Object;
  protected readonly localStorage = localStorage;
}
