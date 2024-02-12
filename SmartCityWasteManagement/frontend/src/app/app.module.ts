import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module'; // Importa il modulo di routing
import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { OfficeAdminDashboardComponent } from './office-admin-dashboard/office-admin-dashboard.component';
import { CompanyAdminDashboardComponent } from './company-admin-dashboard/company-admin-dashboard.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    LoginComponent,
    RegisterComponent,
    AdminDashboardComponent,
    OfficeAdminDashboardComponent,
    CompanyAdminDashboardComponent,
    UserDashboardComponent
  ],
  imports: [
    BrowserModule,
    RouterModule, // Assicurati di importare RouterModule
    AppRoutingModule, // Importa il tuo modulo di routing personalizzato
    FormsModule, // Assicurati di importare FormsModule qui
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
