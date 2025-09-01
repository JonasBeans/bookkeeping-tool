import {Component, inject, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpEventType} from '@angular/common/http';
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {Transaction} from "../../dto/transaction";
import {CostCenterService} from "../services/cost-center.service";
import {MatTableModule} from "@angular/material/table";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOption, MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";


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
		DatePipe,
		MatIcon
	],
	standalone: true,
	styleUrl: "csv-upload.component.css"
})
export class CsvUploadComponent implements OnInit {
	file: File | null = null;
	backupFile: File | null = null;

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

	expand_operations : boolean = false;

	constructor(private http: HttpClient) {}

	ngOnInit() {
		this.retrieveAllTransactions();
	}

	private retrieveAllTransactions() {
		this.http.get<Transaction[]>('http://localhost:8080/transactions/all').subscribe({
			next: (transactions) => this.transactions = transactions,
			error: (error: HttpErrorResponse) => this.error = error.message
		});
	}

	onFileSelected(event: Event) {
		const input = event.target as HTMLInputElement;
		this.file = input.files && input.files.length ? input.files[0] : null;
		this.error = null;
	}

	onBackupFileSelected(event: Event) {
		const input = event.target as HTMLInputElement;
		this.backupFile = input.files && input.files.length ? input.files[0] : null;
		this.error = null;

		if (!this.backupFile) return;

		const formData = new FormData();
		formData.append('file', this.backupFile, this.backupFile.name);

		this.uploading = true;
		this.progress = 0;

		this.http.post<Transaction[]>('http://localhost:8080/synchronization/upload', formData, {
			reportProgress: true,
			observe: 'events'
		}).subscribe({
				next: (response: HttpEvent<Transaction[]>) => console.log(response),
				error: (error: HttpErrorResponse) => console.log(error)
			}
		);
	}

	downloadBackupFiles() {
		this.http.get('http://localhost:8080/synchronization/download', {responseType: 'blob'})
			.subscribe({
				next: (blob) => {
					const url = window.URL.createObjectURL(blob);
					const a = document.createElement('a');
					a.href = url;
					a.download = 'backup.zip'; // Set the file name
					a.click();
					window.URL.revokeObjectURL(url); //
				},
				error: (error: HttpErrorResponse) => console.log(error),
			})
	}

	restore() {
		this.http.put('http://localhost:8080/synchronization/restore', {}, {reportProgress: true, observe: 'events'})
			.subscribe({
				next: respone => this.retrieveAllTransactions(),
				error: (error: HttpErrorResponse) => console.log(error),
			})
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

		this.http.post<String>("http://localhost:8080/synchronization/backup", {})
			.subscribe({
				next: response => this.saving_progress = 100,
				error: response => this.handle_save_error(response),
			});
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
