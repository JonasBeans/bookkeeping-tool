import {Routes} from '@angular/router';
import {CsvUploadComponent} from "./transactions/csv-upload.component";
import {IncomeStatementsComponent} from "./income_statements/income-statements.component";
import {ChartsComponent} from "./charts/charts.component";
import {BalanceComponent} from "./balance/balance.component";
import {LoginComponent} from "./login/login.component";
import {authGuard} from "./auth/auth.guard";

export const routes: Routes = [
	{ path: "login", component: LoginComponent },
	{ path: "transactions", component: CsvUploadComponent, canActivate: [authGuard] },
	{ path: "", component: CsvUploadComponent, canActivate: [authGuard] },
	{ path: "income-statements", component: IncomeStatementsComponent, canActivate: [authGuard] },
	{ path: 'charts', component: ChartsComponent, canActivate: [authGuard] },
	{ path: 'balance', component: BalanceComponent, canActivate: [authGuard] }
];
