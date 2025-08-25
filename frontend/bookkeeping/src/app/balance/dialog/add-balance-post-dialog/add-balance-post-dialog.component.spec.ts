import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddBalancePostDialogComponent} from './add-balance-post-dialog.component';

describe('AddBalancePostDialogComponent', () => {
	let component: AddBalancePostDialogComponent;
	let fixture: ComponentFixture<AddBalancePostDialogComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [AddBalancePostDialogComponent]
		})
			.compileComponents();

		fixture = TestBed.createComponent(AddBalancePostDialogComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
