import {Component} from '@angular/core';
import {CurrencyPipe} from "@angular/common";
import {BalancePostComponent} from "./balance-post/balance-post.component";

@Component({
  selector: 'app-balance',
  standalone: true,
	imports: [
		CurrencyPipe,
		BalancePostComponent
	],
  templateUrl: './balance.component.html',
  styleUrl: './balance.component.css'
})
export class BalanceComponent {
	vaste_activa: string = "Vaste activa";

}
