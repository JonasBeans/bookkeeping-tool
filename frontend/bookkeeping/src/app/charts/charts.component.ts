import {Component, inject, OnInit} from '@angular/core';
import {Color, LegendPosition, NgxChartsModule, ScaleType} from "@swimlane/ngx-charts";
import {ChartService} from "../services/chart.service";
import {CostCenterService} from "../services/cost-center.service";

@Component({
  selector: 'app-charts',
  standalone: true,
	imports: [
		NgxChartsModule
	],
  templateUrl: './charts.component.html',
  styleUrl: './charts.component.css'
})
export class ChartsComponent implements OnInit {

	chart_service: ChartService = inject(ChartService);
	cost_center_service: CostCenterService = inject(CostCenterService);

	// options
	gradient: boolean = true;
	showLegend: boolean = true;
	showLabels: boolean = true;
	isDoughnut: boolean = false;
	legendPosition: LegendPosition = LegendPosition.Below;

	ngOnInit(): void {
		this.cost_center_service.refresh_data();
		this.chart_service.load(this.cost_center_service.costs, this.cost_center_service.incomes);
    }

	colorScheme: Color = {
		name: 'coolScheme',
		selectable: true,
		group: ScaleType.Ordinal,
		domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
	};

	onSelect(data: any): void {
		console.log('Item clicked', JSON.parse(JSON.stringify(data)));
	}

	onActivate(data: any): void {
		console.log('Activate', JSON.parse(JSON.stringify(data)));
	}

	onDeactivate(data: any): void {
		console.log('Deactivate', JSON.parse(JSON.stringify(data)));
	}
}
