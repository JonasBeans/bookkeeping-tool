import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EditBalancePostDialogComponent} from './edit-balance-post-dialog.component';

describe('EditBalancePostDialogComponent', () => {
  let component: EditBalancePostDialogComponent;
  let fixture: ComponentFixture<EditBalancePostDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditBalancePostDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditBalancePostDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
