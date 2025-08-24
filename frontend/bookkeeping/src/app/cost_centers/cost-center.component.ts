import {Component, inject} from '@angular/core';
import {CostCenterService} from "../services/cost-center.service";
import {NgForOf} from "@angular/common";

@Component({
	selector: 'app-cost-center',
	standalone: true,
	imports: [
		NgForOf
	],
	templateUrl: './cost-center.component.html',
	styleUrl: './cost-center.component.css'
})
export class CostCenterComponent {

	costCenterService: CostCenterService = inject(CostCenterService);

}
