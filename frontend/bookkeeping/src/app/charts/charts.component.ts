import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {Color, LegendPosition, NgxChartsModule, ScaleType} from "@swimlane/ngx-charts";
import {ChartService} from "../services/chart.service";
import {CostCenterService} from "../services/cost-center.service";
import {BookYearService} from "../services/book-year.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-charts',
  standalone: true,
	imports: [
		NgxChartsModule
	],
  templateUrl: './charts.component.html',
  styleUrl: './charts.component.css'
})
export class ChartsComponent implements OnInit, OnDestroy {

	chart_service: ChartService = inject(ChartService);
	cost_center_service: CostCenterService = inject(CostCenterService);
	bookYearService: BookYearService = inject(BookYearService);
	private readonly subscriptions = new Subscription();

	// options
	gradient: boolean = true;
	showLegend: boolean = true;
	showLabels: boolean = true;
	isDoughnut: boolean = false;
	legendPosition: LegendPosition = LegendPosition.Below;

	ngOnInit(): void {
		this.subscriptions.add(this.bookYearService.selectedBookYear$.subscribe(bookYear => this.cost_center_service.refresh_data(bookYear)));
		this.subscriptions.add(this.cost_center_service.costCenters$.subscribe(() => {
			this.chart_service.load(this.cost_center_service.costs, this.cost_center_service.incomes);
		}));
	}

	ngOnDestroy(): void {
		this.subscriptions.unsubscribe();
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
