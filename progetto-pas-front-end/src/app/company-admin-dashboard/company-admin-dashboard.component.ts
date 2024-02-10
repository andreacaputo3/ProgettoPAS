import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {environment} from "../../enviroments/enviroments";

@Component({
  selector: 'app-company-admin-dashboard',
  templateUrl: './company-admin-dashboard.component.html',
  styleUrls: ['./company-admin-dashboard.component.scss']
})
export class CompanyAdminDashboardComponent {
  checkResult: string = '';
  binsForMap: any[] = [];
  cleaningPaths: any[] = [];
  loadingData: boolean = false;
  overloadedBins: any[] = [];
  successMessage = '';

  constructor(private router: Router, private http: HttpClient) {}

  checkWasteDisposals(): void {
    this.resetData();
    let url = `${environment.apiUrl}/waste-company/check-waste-disposals`;
    this.http.post<any>(url, {}, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          this.checkResult = response.message;
          this.overloadedBins = response.overloadedBins;
          this.loadingData = false;
        },
        error: (error) => {
          console.error('Errore durante il controllo dei conferimenti:', error);
          this.loadingData = false;
        }
      });
  }

  getBinsForMap(): void {
    this.resetData();
    let url = `${environment.apiUrl}/waste-company/map`;
    this.http.get<any[]>(url, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          this.binsForMap = response;
          this.loadingData = false;
        },
        error: (error) => {
          console.error('Errore durante il recupero dei cassonetti per la mappa:', error);
          this.loadingData = false;
        }
      });
  }

  instantiateCleaningPaths(): void {
    this.resetData();
    let url = `${environment.apiUrl}/waste-company/cleaning-paths`;
    this.http.post<any[]>(url, {}, { headers: this.getHeaders() } )
      .subscribe({
        next: (response) => {
          console.log(response);
          this.cleaningPaths = response;
          this.loadingData = false;
        },
        error: (error) => {
          console.error('Errore durante l\'istanziazione dei percorsi di pulizia:', error);
          this.loadingData = false;
        }
      });
  }

  emptyBin(binId: string): void {
    console.log(binId);
    let url = `${environment.apiUrl}/waste-company/${binId}/empty`;
    this.http.post<string>(url, {}, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          this.successMessage = "Svuotamento eseguito";
          this.loadingData = false;
        },
        error: (error) => {
          console.error('Errore durante lo svuotamento del cassonetto:', error);
          this.loadingData = false;
        }
      });
  }

  private resetData(): void {
    this.checkResult = '';
    this.binsForMap = [];
    this.cleaningPaths = [];
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
}
