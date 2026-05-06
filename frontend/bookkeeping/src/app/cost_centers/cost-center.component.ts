import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {CostCenterService} from "../services/cost-center.service";
import {NgForOf} from "@angular/common";
import {BookYearService} from "../services/book-year.service";
import {Subscription} from "rxjs";

@Component({
	selector: 'app-cost-center',
	standalone: true,
	imports: [
		NgForOf
	],
	templateUrl: './cost-center.component.html',
	styleUrl: './cost-center.component.css'
})
export class CostCenterComponent implements OnInit, OnDestroy {

	costCenterService: CostCenterService = inject(CostCenterService);
	bookYearService: BookYearService = inject(BookYearService);
	private readonly subscriptions = new Subscription();

	ngOnInit(): void {
		this.subscriptions.add(this.bookYearService.selectedBookYear$.subscribe(bookYear => this.costCenterService.refresh_data(bookYear)));
	}

	ngOnDestroy(): void {
		this.subscriptions.unsubscribe();
	}

}
