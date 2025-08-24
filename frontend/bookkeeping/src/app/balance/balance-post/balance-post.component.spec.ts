import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BalancePostComponent} from './balance-post.component';

describe('BalancePostComponent', () => {
  let component: BalancePostComponent;
  let fixture: ComponentFixture<BalancePostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BalancePostComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BalancePostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
