import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NewCostCenterDialog} from './new-cost-center-dialog.component';

describe('DialogComponent', () => {
  let component: NewCostCenterDialog;
  let fixture: ComponentFixture<NewCostCenterDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewCostCenterDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewCostCenterDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
