import {Component, inject} from '@angular/core';
import {CostCenterService} from "../services/cost-center.service";
import {CurrencyPipe, NgForOf} from "@angular/common";
import {CostCenter} from "../../dto/cost-center";

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

	cost: CostCenter[]= [];
	income: CostCenter[]= [];
	costCenterService: CostCenterService = inject(CostCenterService);

}
