import {inject, Injectable} from '@angular/core';
import {CostCenter} from "../../dto/cost-center";
import {HttpClient} from "@angular/common/http";

@Injectable({
	providedIn: 'root'
})
export class CostCenterService {

	public all: CostCenter[] = [];
	public costs: CostCenter[] = [];
	public incomes: CostCenter[] = [];
	public client: HttpClient = inject(HttpClient);

	constructor() {
		this.client.get<CostCenter[]>('http://localhost:8080/api/cost-centers/all')
			.subscribe((response) => {
				this.all = response;
				this.costs = this.all.filter(item => item.isCost);
				this.incomes = this.all.filter(item => !item.isCost)
			})
	}

}
