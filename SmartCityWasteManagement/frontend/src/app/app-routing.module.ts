import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component'; // Assumi che ci sia un componente di login
import { RegisterComponent } from './register/register.component'; // Assumi che ci sia un componente di registrazione
import { HomepageComponent } from './homepage/homepage.component';
import {AdminDashboardComponent} from "./admin-dashboard/admin-dashboard.component";
import {CompanyAdminDashboardComponent} from "./company-admin-dashboard/company-admin-dashboard.component";
import {OfficeAdminDashboardComponent} from "./office-admin-dashboard/office-admin-dashboard.component";
import {UserDashboardComponent} from "./user-dashboard/user-dashboard.component";

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent },
  { path: 'office-admin-dashboard', component: OfficeAdminDashboardComponent },
  { path: 'company-admin-dashboard', component: CompanyAdminDashboardComponent },
  { path: 'user-dashboard', component: UserDashboardComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
