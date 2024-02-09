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
  loadingData: boolean = false; // Aggiunta variabile per il caricamento dei dati
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
          console.log(response.message);
          console.log(response.overloadedBins);
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
    // Rimuovi le credenziali memorizzate nel localStorage o esegui altre operazioni di logout necessarie
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');

    // Reindirizza l'utente alla pagina di accesso o ad un'altra pagina appropriata
    // Puoi utilizzare il Router per navigare a una nuova pagina
    this.router.navigate(['/login']); // Assicurati di importare il Router e di iniettarlo nel costruttore del componente
  }


  protected readonly localStorage = localStorage;
}
