import {Component, inject, OnInit, signal} from '@angular/core';
import {CostCenterService} from "../services/cost-center.service";
import {CurrencyPipe, NgForOf} from "@angular/common";
import {NewCostCenterDialog} from "./dialog/new-cost-center-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-income-statements',
  standalone: true,
	imports: [
		NgForOf,
		CurrencyPipe,
		MatIcon
	],
  templateUrl: './income-statements.component.html',
  styleUrl: './income-statements.component.css'
})
export class IncomeStatementsComponent implements OnInit {
	readonly new_cost_center_title = signal('');
	readonly is_new_cost_center_a_cost = signal(true);
	cost_center_service: CostCenterService = inject(CostCenterService);
	readonly dialog = inject(MatDialog);

	ngOnInit(): void {
		this.cost_center_service.refresh_data();
	}

	opendialog() {
		const dialogRef = this.dialog.open(NewCostCenterDialog, {
			data: {new_cost_center_title: this.new_cost_center_title(), is_new_cost_center_a_cost: this.is_new_cost_center_a_cost()}
		});
		dialogRef.afterClosed().subscribe(result => {
			console.log('The dialog was closed');
			this.new_cost_center_title.set(result.new_cost_center_title);
			this.is_new_cost_center_a_cost.set(result.is_new_cost_center_a_cost);
			console.log(result)
			console.log(this.new_cost_center_title())
			console.log(result.new_cost_center_title);
			this.cost_center_service.addCostCenter(this.new_cost_center_title(), this.is_new_cost_center_a_cost());
		});
	}
}
