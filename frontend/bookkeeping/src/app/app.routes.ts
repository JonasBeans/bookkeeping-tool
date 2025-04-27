import { Routes } from '@angular/router';
import {CsvUploadComponent} from "./transactions/csv-upload/csv-upload.component";

export const routes: Routes = [
	{ path: "transactions", component: CsvUploadComponent },
	{ path: "", component: CsvUploadComponent },
];
