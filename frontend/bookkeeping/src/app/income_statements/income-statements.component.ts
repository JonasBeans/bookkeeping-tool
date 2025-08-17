import {Component, inject} from '@angular/core';
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
export class IncomeStatementsComponent {

	costCenterService: CostCenterService = inject(CostCenterService);

}
