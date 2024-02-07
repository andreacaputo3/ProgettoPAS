import { Component } from '@angular/core';
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


  constructor(private http: HttpClient) {}

  checkWasteDisposals(): void {
    this.resetData();
    let url = `${environment.apiUrl}/waste-company/check-waste-disposals`;
    this.http.post(url, {}, { headers: this.getHeaders(), responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.checkResult = response;
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
    this.http.post<string>(`/api/waste-company/${binId}/empty`, {})
      .subscribe({
        next: (response) => {
          console.log(response); // Puoi gestire la risposta qui, se necessario
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

  protected readonly localStorage = localStorage;
}
