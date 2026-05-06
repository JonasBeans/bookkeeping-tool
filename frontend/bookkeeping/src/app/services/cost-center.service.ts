import {isPlatformBrowser} from "@angular/common";
import {inject, Injectable, PLATFORM_ID} from '@angular/core';
import {CostCenter} from "../../dto/cost-center";
import {HttpClient, HttpParams} from "@angular/common/http";
import {AccumulatedAmounts} from "../../dto/accumulated-amounts";
import {environment} from "../../environments/environment";
import {BehaviorSubject} from "rxjs";
import {BookYearService} from "./book-year.service";

@Injectable({
	providedIn: 'root'
})
export class CostCenterService {

	public all: CostCenter[] = [];
	public costs: CostCenter[] = [];
	public incomes: CostCenter[] = [];
	public accumulatedAmounts: AccumulatedAmounts = {totalIncome: 0, totalCost: 0};
	public client: HttpClient = inject(HttpClient);
	private readonly bookYearService: BookYearService = inject(BookYearService);
	private readonly platformId: Object = inject(PLATFORM_ID);
	private readonly costCentersSubject = new BehaviorSubject<CostCenter[]>([]);
	public readonly costCenters$ = this.costCentersSubject.asObservable();
	private readonly baseUrl = environment.apiBaseUrl; // empty string means same-origin

	constructor() {
		this.refresh_data();
	}

	public refresh_data(bookYear: number | null = this.bookYearService.selectedBookYear): void {
		if (!isPlatformBrowser(this.platformId)) {
			return;
		}
		this.getCostCenters(bookYear);
		this.getAccumulatedAmounts(bookYear);
	}

	private api(path: string): string { // ensure leading slash
		return `${this.baseUrl}${path.startsWith('/') ? path : '/' + path}`;
	}

	addCostCenter(cost_center_title: string, isCost: boolean) : void {
		this.client.post(
			this.api('/api/cost-centers/add'),
			{title: cost_center_title, isCost: isCost}
		).subscribe({
			next: result => {
				console.log('Cost center added successfully:', result);
				this.refresh_data();
			},
			error: error => {
				console.error('Error adding cost center:', error);
			}
		});
	}

	private getCostCenters(bookYear: number | null): void {
		this.client.get<CostCenter[]>(this.api('/api/cost-centers/all'), this.createRequestOptions(bookYear))
			.subscribe((response) => {
				this.all = response;
				this.costs = this.all.filter(item => item.isCost);
				this.incomes = this.all.filter(item => !item.isCost);
				this.costCentersSubject.next(response);
			})
	}

	private getAccumulatedAmounts(bookYear: number | null): void {
		this.client.get<AccumulatedAmounts>(this.api('/api/cost-centers/accumulated-amounts'), this.createRequestOptions(bookYear))
			.subscribe({
				next: value => this.accumulatedAmounts = value,
				error: err => {
					console.error('Error fetching accumulated amounts:', err);
					this.accumulatedAmounts = {totalIncome: 0, totalCost: 0};
				}
			});
	}

	private createRequestOptions(bookYear: number | null): { params?: HttpParams } {
		if (bookYear === null) {
			return {};
		}
		return {params: new HttpParams().set('bookYear', bookYear)};
	}

}
