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
  binsData: any;
  usersData: any;

  loadingUsers = false;
  loadingBins = false;
  loadingUsersToAware = false;
  loadingPaymentsState = false;
  loadingWasteSeparationPerformance = false;
  loadingYearlyPaymentAmounts = false;
  loadingBinForm: boolean = false;

  errorMessage = '';
  responseMessage = '';
  newBin: any = {
    location: null,
    latitude: null,
    longitude: '',
    type: '',
    maxWeight: null
  };

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers(): void {
    this.loadingUsers = true;
    let url = `${environment.apiUrl}/users/get-all`;
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

  showBinForm(): void {
    this.resetData();
    this.loadingBinForm = true;
  }
  createBin(): void {
    if (!this.newBin || !this.newBin.type || !this.newBin.maxWeight || !this.newBin.location || !this.newBin.longitude || !this.newBin.latitude) {
      console.error('Compilare tutti i campi prima di conferire il rifiuto.');
      this.errorMessage = "Compilare tutti i campi prima di creare il cassonetto.";
      return;
    }
    let url = `${environment.apiUrl}/bins/create`;
    console.log(this.newBin);
    this.http.post<any>(url, this.newBin, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.errorMessage = '';
        this.responseMessage = 'Cassonetto inserito.';
        this.newBin.location = null;
        this.newBin.latitude = null;
        this.newBin.longitude = '';
        this.newBin.type = '';
        this.newBin.maxWeight = null;
      },
      error: (error) => {
        console.error('Errore durante la creazione del cassonetto:', error);
        this.errorMessage = 'Errore inserimento';
        this.responseMessage = '';
      }
    });
  }

  deleteUser(userId: string): void {
    if (confirm('Sei sicuro di voler eliminare questo utente?')) {
      let url = `${environment.apiUrl}/users/delete/${userId}`;
      this.http.delete<any>(url, { headers: this.getHeaders() }).subscribe({
        next: (response) => {
          console.log('Utente eliminato con successo:', response);
          this.getAllUsers();
          // Aggiorna la lista degli utenti o esegui altre operazioni necessarie dopo l'eliminazione
        },
        error: (error) => {
          console.error('Errore durante l\'eliminazione dell\'utente:', error);
          // Gestisci l'errore in base alle tue esigenze
        }
      });
    }
  }

  getAllBinLocations(): void {
    this.resetData();
    this.loadingBins = true;
    let url = `${environment.apiUrl}/bins/`;
    this.http.get<any>(url, { headers: this.getHeaders() }).subscribe({
      next: (response) => {
        this.binsData = response;
        this.loadingBins = false;
      },
      error: (error) => {
        console.error('Errore durante il recupero dei cassonetti:', error);
        this.loadingBins = false;
      }
    });
  }

  private resetData(): void {
    this.loadingBinForm = false;
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
    // Rimuovo credenziali memorizzate nel localStorage
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');

    // Reindirizzo utente al login
    this.router.navigate(['/login']);
  }

  protected readonly localStorage = localStorage;
  protected readonly Object = Object;
}
