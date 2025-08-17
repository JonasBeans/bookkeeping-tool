import {Component, inject, OnInit} from '@angular/core';
import {CostCenterService} from "../services/cost-center.service";
import {CurrencyPipe, NgForOf} from "@angular/common";

@Component({
  selector: 'app-income-statements',
  standalone: true,
	imports: [
		NgForOf,
		CurrencyPipe
	],
  templateUrl: './income-statements.component.html',
  styleUrl: './income-statements.component.css'
})
export class IncomeStatementsComponent implements OnInit {

	costCenterService: CostCenterService = inject(CostCenterService);

	ngOnInit(): void {
		this.costCenterService.refresh_data();
	}
}
