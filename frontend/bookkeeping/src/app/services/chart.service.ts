import {Injectable} from '@angular/core';
import {CostCenter} from "../../dto/cost-center";
import {ChartPart} from "../../model/chartPart";

@Injectable({
	providedIn: 'root'
})
export class ChartService {

	public costs_pie_chart_data: ChartPart[] = [];
	public income_pie_chart_data: ChartPart[] = [];

	constructor() {}

	public load(costs: CostCenter[], incomes: CostCenter[]): void {
		this.costs_pie_chart_data = costs
			.filter(item => item.totalAmount > 0)
			.filter(item => item.costCenter.toLowerCase() != 'skip')
			.map<ChartPart>(item => {return {name: item.costCenter, value: item.totalAmount}});
		this.income_pie_chart_data = incomes
			.filter(item => item.totalAmount > 0)
			.map<ChartPart>(item => {return {name: item.costCenter, value: item.totalAmount}});
	}



}
