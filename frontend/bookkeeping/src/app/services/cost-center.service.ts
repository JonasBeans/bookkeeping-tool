import {inject, Injectable} from '@angular/core';
import {CostCenter} from "../../dto/cost-center";
import {HttpClient} from "@angular/common/http";
import {AccumulatedAmounts} from "../../dto/accumulated-amounts";
import {environment} from "../../environments/environment";

@Injectable({
	providedIn: 'root'
})
export class CostCenterService {

	public all: CostCenter[] = [];
	public costs: CostCenter[] = [];
	public incomes: CostCenter[] = [];
	public accumulatedAmounts: AccumulatedAmounts = {totalIncome: 0, totalCost: 0};
	public client: HttpClient = inject(HttpClient);
	private readonly baseUrl = environment.apiBaseUrl; // empty string means same-origin

	constructor() {
		this.refresh_data();
	}

	public refresh_data(): void {
		this.getCostCenters();
		this.getAccumulatedAmounts();
	}

	private api(path: string): string { // ensure leading slash
		return `${this.baseUrl}${path.startsWith('/') ? path : '/' + path}`;
	}

	private getCostCenters(): void {
		this.client.get<CostCenter[]>(this.api('/api/cost-centers/all'))
			.subscribe((response) => {
				this.all = response;
				this.costs = this.all.filter(item => item.isCost);
				this.incomes = this.all.filter(item => !item.isCost)
			})
	}

	private getAccumulatedAmounts(): void {
		this.client.get<AccumulatedAmounts>(this.api('/api/cost-centers/accumulated-amounts'))
			.subscribe({
				next: value => this.accumulatedAmounts = value,
				error: err => {
					console.error('Error fetching accumulated amounts:', err);
					this.accumulatedAmounts = {totalIncome: 0, totalCost: 0}; // Default values in case of error
				}
			});
	}

	addCostCenter(cost_center_title: string, isCost: boolean) : void {
		this.client.post(
			this.api('/api/cost-centers/add'),
			{title: cost_center_title, isCost: isCost}
		).subscribe({
			next: result => {
				console.log('Cost center added successfully:', result);
				this.refresh_data(); // Refresh data after adding a new cost center
			},
			error: error => {
				console.error('Error adding cost center:', error);
			}
		});
	}

}
