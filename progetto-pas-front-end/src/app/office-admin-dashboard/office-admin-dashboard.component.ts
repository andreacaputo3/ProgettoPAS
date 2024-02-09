import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import {environment} from "../../enviroments/enviroments";

@Component({
  selector: 'app-office-admin-dashboard',
  templateUrl: './office-admin-dashboard.component.html',
  styleUrls: ['./office-admin-dashboard.component.scss']
})
export class OfficeAdminDashboardComponent {
  usersToAwareData: any;
  paymentsStateData: any;
  wasteSeparationPerformanceData: any;
  yearlyPaymentAmountsData: any;

  loadingUsersToAware = false;
  loadingPaymentsState = false;
  loadingWasteSeparationPerformance = false;
  loadingYearlyPaymentAmounts = false;

  constructor(private router: Router, private http: HttpClient) {}

  getUsersToAware(): void {
    this.resetData();
    this.loadingUsersToAware = true;
    let url = `${environment.apiUrl}/municipal-office/users-to-aware`;
    this.http.get<any>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.usersToAwareData = response;
        this.loadingUsersToAware = false;
      },
      error: (error) => {
        console.error('Errore durante il recupero degli utenti da sensibilizzare:', error);
        this.loadingUsersToAware = false;
      }
    });
  }

  getPaymentsState(): void {
    this.resetData();
    this.loadingPaymentsState = true;
    let url = `${environment.apiUrl}/municipal-office/payments-state`;
    this.http.get<any>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.paymentsStateData = response;
        this.loadingPaymentsState = false;
      },
      error: (error) => {
        console.error('Errore durante il recupero dello stato dei pagamenti:', error);
        this.loadingPaymentsState = false;
      }
    });
  }

  getWasteSeparationPerformance(): void {
    this.resetData();
    this.loadingWasteSeparationPerformance = true;
    let url = `${environment.apiUrl}/municipal-office/waste-separation-performance`;
    this.http.get<any>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.wasteSeparationPerformanceData = response;
        this.loadingWasteSeparationPerformance = false;
      },
      error: (error) => {
        console.error('Errore durante il recupero delle performance di separazione dei rifiuti:', error);
        this.loadingWasteSeparationPerformance = false;
      }
    });
  }

  calculateYearlyPaymentAmounts(): void {
    this.resetData();
    this.loadingYearlyPaymentAmounts = true;
    let url = `${environment.apiUrl}/municipal-office/yearly-payment-amounts`;
    this.http.get<any>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.yearlyPaymentAmountsData = response;
        this.loadingYearlyPaymentAmounts = false;
      },
      error: (error) => {
        console.error('Errore durante il calcolo dei pagamenti annuali:', error);
        this.loadingYearlyPaymentAmounts = false;
      }
    });
  }

  erogateYearlyPayments(): void {
    this.resetData();
    let url = `${environment.apiUrl}/municipal-office/erogate-payments`;
    this.http.post<any>(url, {}, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        console.log('Pagamenti annuali erogati con successo:', response);
        this.calculateYearlyPaymentAmounts();
      },
      error: (error) => {
        console.error('Errore durante l\'erogazione dei pagamenti annuali:', error);
        this.loadingYearlyPaymentAmounts = false;
      }
    });
  }

  private resetData(): void {
    this.usersToAwareData = null;
    this.paymentsStateData = null;
    this.wasteSeparationPerformanceData = null;
    this.yearlyPaymentAmountsData = null;
  }

  private getHeaders(): HttpHeaders {
    let headers = new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('jwtToken')}`);
    headers = headers.append("Content-Type", "application/json");
    return headers;
  }

  logout(): void {
    // Rimuovi le credenziali memorizzate nel localStorage o esegui altre operazioni di logout necessarie
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');

    // Reindirizza l'utente alla pagina di accesso o ad un'altra pagina appropriata
    // Puoi utilizzare il Router per navigare a una nuova pagina
    this.router.navigate(['/login']); // Assicurati di importare il Router e di iniettarlo nel costruttore del componente
  }

  protected readonly localStorage = localStorage;
  protected readonly Object = Object;
}
