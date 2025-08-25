import {Component} from '@angular/core';
import {BalancePostComponent} from "./balance-post/balance-post.component";

@Component({
  selector: 'app-balance',
  standalone: true,
	imports: [
		BalancePostComponent
	],
  templateUrl: './balance.component.html',
  styleUrl: './balance.component.css'
})
export class BalanceComponent {

	vaste_activa_title: string = "Vaste activa";
	vlottende_activa_title: string = "Vlottende activa";
	liquide_middelen_title: string = "Liquide middelen";
	eigen_vermogen_title: string = "Eigen vermogen";
	kortlopende_schulden_title: string = 'Kortlopende schulden';
	langlopende_schulden_title: string = 'Langlopende schulden';

}
