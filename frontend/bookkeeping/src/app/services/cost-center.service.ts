import {inject, Injectable} from '@angular/core';
import {CostCenter} from "../../dto/cost-center";
import {HttpClient} from "@angular/common/http";
import {AccumulatedAmounts} from "../../dto/accumulated-amounts";

@Injectable({
	providedIn: 'root'
})
export class CostCenterService {

	public all: CostCenter[] = [];
	public costs: CostCenter[] = [];
	public incomes: CostCenter[] = [];
	public accumulatedAmounts: AccumulatedAmounts = {totalIncome: 0, totalCost: 0};
	public client: HttpClient = inject(HttpClient);

	constructor() {
		this.refresh_data();
	}

	public refresh_data(): void {
		this.getCostCenters();
		this.getAccumulatedAmounts();
	}

	private getCostCenters(): void {
		this.client.get<CostCenter[]>('http://localhost:8080/api/cost-centers/all')
			.subscribe((response) => {
				this.all = response;
				this.costs = this.all.filter(item => item.isCost);
				this.incomes = this.all.filter(item => !item.isCost)
			})
	}

	private getAccumulatedAmounts(): void {
		this.client.get<AccumulatedAmounts>('http://localhost:8080/api/cost-centers/accumulated-amounts')
			.subscribe({
				next: value => this.accumulatedAmounts = value,
				error: err => {
					console.error('Error fetching accumulated amounts:', err);
					this.accumulatedAmounts = {totalIncome: 0, totalCost: 0}; // Default values in case of error
				}
			});
	}

}
