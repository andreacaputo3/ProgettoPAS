import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeAdminDashboardComponent } from './office-admin-dashboard.component';

describe('OfficeAdminDashboardComponent', () => {
  let component: OfficeAdminDashboardComponent;
  let fixture: ComponentFixture<OfficeAdminDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OfficeAdminDashboardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OfficeAdminDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
