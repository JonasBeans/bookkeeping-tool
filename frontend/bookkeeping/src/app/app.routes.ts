import {Routes} from '@angular/router';
import {CsvUploadComponent} from "./transactions/csv-upload.component";
import {IncomeStatementsComponent} from "./income_statements/income-statements.component";
import {ChartsComponent} from "./charts/charts.component";
import {BalanceComponent} from "./balance/balance.component";

export const routes: Routes = [
	{ path: "transactions", component: CsvUploadComponent },
	{ path: "", component: CsvUploadComponent },
	{ path: "income-statements", component: IncomeStatementsComponent },
	{ path: 'charts', component: ChartsComponent },
	{ path: 'balance', component: BalanceComponent }
];
