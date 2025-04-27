import {Component, inject} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpEventType} from '@angular/common/http';
import {NgForOf, NgIf} from "@angular/common";
import {Transaction} from "../../../dto/transaction";
import {CostCenterService} from "../../services/cost-center.service";
import {MatTableModule} from "@angular/material/table";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOption, MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";


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
		NgForOf
	],
	standalone: true,
	styleUrl: "csv-upload.component.css"
})
export class CsvUploadComponent {
	file: File | null = null;
	progress = -1;
	uploading = false;
	error: string | null = null;
	transactions: Transaction[] = [];
	costCenterService: CostCenterService = inject(CostCenterService);
	displayedColumns: string[] = ['bookDate','transactionDate', 'amount', 'nameOtherParty', 'costCenter'];

	constructor(private http: HttpClient) {
	}

	onFileSelected(event: Event) {
		const input = event.target as HTMLInputElement;
		this.file = input.files && input.files.length ? input.files[0] : null;
		this.error = null;
	}

	upload() {
		if (!this.file) return;

		const formData = new FormData();
		formData.append('file', this.file, this.file.name);

		this.uploading = true;
		this.progress = 0;

		this.http.post('http://localhost:8080/transactions/upload/transaction-file', formData, {
			reportProgress: true,
			observe: 'events'
		}).subscribe({
				next: (response) => this.execute_upload(response),
				error: (error: HttpErrorResponse) => this.handle_upload_error(error)
			}
		);
	}

	execute_upload(event: HttpEvent<Object>) {
		if (event.type === HttpEventType.UploadProgress && event.total) {
			this.progress = Math.round((event.loaded / event.total) * 100);
		} else if (event.type === HttpEventType.Response) {
			this.progress = 100;
			this.uploading = false;
		}
		this.load_transaction();
		console.log(this.transactions);
	}

	handle_upload_error(error: HttpErrorResponse) {
		this.error = error.message;
		this.uploading = false;
		this.progress = -1;
	}

	load_transaction() {
		this.http.get<Transaction[]>("http://localhost:8080/transactions/all").subscribe((response) => {
			this.transactions = response
		})
	}

}
