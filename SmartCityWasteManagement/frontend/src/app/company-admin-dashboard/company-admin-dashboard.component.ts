import { Component } from '@angular/core';
import {Router} from '@angular/router';
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
  overloadedBins: any[] = [];
  selectedBins: any[] = [];
  routeData: any[] = [];
  pathName: string = '';
  routeNameToDelete= '';

  mapType = 'roadmap';

  loadingData: boolean = false;

  successMessage = '';
  successMessagePath = '';
  noPathMessage = '';

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    this.getAllBinLocations();
  }

  checkWasteDisposals(): void {
    this.loadingData = true;
    this.resetData();
    let url = `${environment.apiUrl}/waste-company/check-waste-disposals`;
    this.http.post<any>(url, {}, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          this.checkResult = response.message;
          this.overloadedBins = response.overloadedBins;
          this.getAllBinLocations();
          this.successMessagePath = '';
          this.noPathMessage = '';
          this.loadingData = false;
        },
        error: (error) => {
          console.error('Errore durante il controllo dei conferimenti:', error);
          this.loadingData = false;
        }
      });
  }

  getAllBinLocations(): void {
    this.loadingData = true;
    let url = `${environment.apiUrl}/bins/`;
    this.http.get<any[]>(url, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          this.binsForMap = response;
          this.loadingData = false;
        },
        error: (error) => {
          console.error('Errore durante il recupero di tutti i cassonetti:', error);
          this.loadingData = false;
        }
      });
  }

  emptyBin(binId: string): void {
    this.loadingData = true;
    let url = `${environment.apiUrl}/waste-company/${binId}/empty`;
    this.http.post<string>(url, {}, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          this.successMessage = "Cassonetto svuotato con successo!";
          this.loadingData = false;
          this.successMessagePath = '';
        },
        error: (error) => {
          console.error('Errore durante lo svuotamento del cassonetto:', error);
          this.loadingData = false;
        }
      });
  }

  getAllCleaningPaths(): void {
    this.loadingData = true;
    this.resetData();
    let url = `${environment.apiUrl}/waste-company/get-cleaning-paths`;
    this.http.get<any[]>(url, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          if (response && response.length > 0) {
            this.routeData = response;
            this.getAllBinLocations();
            this.noPathMessage = '';
            this.successMessagePath = '';
          } else {
            this.noPathMessage = 'Nessun percorso di pulizia disponibile.';
            this.getAllBinLocations();
          }
          this.loadingData = false;
        },
        error: (error) => {
          console.error('Errore durante il recupero dei percorsi di pulizia:', error);
          this.loadingData = false;
        }
      });
  }


  selectBin(bin: any): void {
    const index = this.selectedBins.findIndex(selectedBin => selectedBin.id === bin.id);
    if (index === -1) {
      this.selectedBins.push(bin); // Aggiungi il cassonetto alla lista dei selezionati
      // Rimuovi il cassonetto dalla lista principale
      const binIndex = this.binsForMap.findIndex(b => b.id === bin.id);
      if (binIndex !== -1) {
        this.binsForMap.splice(binIndex, 1);
      }
    }
  }

  removeSelectedBin(selectedBin: any): void {
    const index = this.selectedBins.findIndex(bin => bin.id === selectedBin.id);
    if (index !== -1) {
      // Rimuovo cassonetto dalla lista dei selezionati
      this.selectedBins.splice(index, 1);
      // Aggiungo cassonetto alla lista principale
      this.binsForMap.push(selectedBin);
    }
  }

  saveCleaningPaths(): void {
    if (!this.pathName) {
      alert('Inserisci il nome del percorso da cancellare');
      return;
    }
    this.loadingData = true;
    let url = `${environment.apiUrl}/waste-company/cleaning-paths`;

    let body = {
      binIds: this.selectedBins.map(bin => bin.id),
      pathName: this.pathName
    };

    this.http.post<any>(url, body, { headers: this.getHeaders() })
      .subscribe({
        next: (response) => {
          this.resetData();
          this.getAllBinLocations();
          this.selectedBins = [];
          this.loadingData = false;
          this.successMessagePath = "Nuovo percorso di pulizia aggiunto";
          this.pathName = '';
        },
        error: (error) => {
          console.error('Errore durante la generazione dei percorsi di pulizia:', error);
        }
      });
  }

  deleteRouteByName(): void {
    if (!this.routeNameToDelete) {
      alert('Inserisci il nome del percorso da cancellare');
      return;
    }
    if (confirm('Sei sicuro di voler cancellare il percorso "' + this.routeNameToDelete + '"?')) {
      let url = `${environment.apiUrl}/waste-company/delete/${this.routeNameToDelete}`;
      this.http.delete<any>(url, { headers: this.getHeaders() }).subscribe({
        next: (response) => {
          this.routeNameToDelete = '';
          this.successMessagePath = '';
          this.getAllCleaningPaths();
        },
        error: (error) => {
          console.error('Errore durante l\'eliminazione del percorso:', error);
        }
      });
    }
  }

  private resetData(): void {
    this.checkResult = '';
    this.binsForMap = [];
    this.routeData = [];
    this.overloadedBins = [];
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
