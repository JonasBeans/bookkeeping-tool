import {Component, inject} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpEventType} from '@angular/common/http';
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {Transaction} from "../../../dto/transaction";
import {CostCenterService} from "../../services/cost-center.service";
import {MatTableModule} from "@angular/material/table";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOption, MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";
import {BalanceSheetComponent} from "../../balance_sheet/balance-sheet/balance-sheet.component";


@Component({
	selector: 'app-csv-upload',
	templateUrl: './csv-upload.component.html',
	imports: [
		NgIf,
		MatTableModule,
		MatFormFieldModule,
		MatSelectModule,
		MatOption,
		FormsModule,
		NgForOf,
		BalanceSheetComponent,
		DatePipe
	],
	standalone: true,
	styleUrl: "csv-upload.component.css"
})
export class CsvUploadComponent {
	file: File | null = null;

	progress = -1;
	uploading = false;
	error: string | null = null;

	assign_upload: boolean = false;
	assign_error: string = "";

	saving: boolean = false;
	save_error: string = "";
	saving_progress: number = -1;

	transactions: Transaction[] = [];
	costCenterService: CostCenterService = inject(CostCenterService);
	displayedColumns: string[] = ['bookDate','transactionDate', 'amount', 'nameOtherParty', 'costCenter'];


	constructor(private http: HttpClient) {}

	onFileSelected(event: Event) {
		const input = event.target as HTMLInputElement;
		this.file = input.files && input.files.length ? input.files[0] : null;
		this.error = null;
	}

	assign() {
		this.http.put<Transaction[]>('http://localhost:8080/transactions/assigned', this.transactions, {reportProgress: true, observe: 'events'})
			.subscribe({
				next: (response: HttpEvent<Transaction[]>) => this.assign_callback(response),
				error: (error) => this.handle_assign_error(error)
			});
	}

	upload() {
		if (!this.file) return;

		const formData = new FormData();
		formData.append('file', this.file, this.file.name);

		this.uploading = true;
		this.progress = 0;

		this.http.post<Transaction[]>('http://localhost:8080/transactions/upload/transaction-file', formData, {
			reportProgress: true,
			observe: 'events'
		}).subscribe({
				next: (response: HttpEvent<Transaction[]>) => this.upload_callback(response),
				error: (error: HttpErrorResponse) => this.handle_upload_error(error)
			}
		);
	}

	save() {
		this.saving = true;
		this.saving_progress = 0;

		this.http.put<any>("http://localhost:8080/transactions/save-to-file", {}, {reportProgress: true, observe: "events"}).subscribe({
			next: (response : HttpEvent<any>) => this.save_callback(response),
			error: (error: HttpErrorResponse) => this.handle_save_error(error)
		})
	}

	upload_callback(event: HttpEvent<Transaction[]>) {
		if (event.type === HttpEventType.UploadProgress && event.total) {
			this.progress = Math.round((event.loaded / event.total) * 100);
		} else if (event.type === HttpEventType.Response) {
			this.progress = 100;
			this.uploading = false;
			this.transactions = event.body || [];
		}
	}

	handle_upload_error(error: HttpErrorResponse) {
		this.error = error.message;
		this.uploading = false;
		this.progress = -1;
	}

	assign_callback(event: HttpEvent<Transaction[]>) {
		console.log(event.type)
		if (event.type === HttpEventType.Response) {
			this.assign_upload = true;
		}
	}

	handle_assign_error(error: HttpErrorResponse) {
		this.assign_error = error.message;
		this.assign_upload = false;
	}

	save_callback(event: HttpEvent<any>) {
		if (event.type === HttpEventType.UploadProgress && event.total) {
			this.progress = Math.round((event.loaded / event.total) * 100)
		} else if (event.type === HttpEventType.Response) {
			this.saving = false;
		}
	}

	handle_save_error(error: HttpErrorResponse) {
		this.save_error = error.error;
		this.saving = false;
		this.saving_progress = -1;
	}

	protected readonly CostCenterService = CostCenterService;
}
