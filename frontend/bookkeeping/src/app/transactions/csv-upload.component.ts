import {Component, inject, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent} from '@angular/common/http';
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {Transaction} from "../../dto/transaction";
import {CostCenterService} from "../services/cost-center.service";
import {MatTableModule} from "@angular/material/table";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOption, MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";
import {environment} from "../../environments/environment";
import {MatExpansionPanel, MatExpansionPanelHeader, MatExpansionPanelTitle} from "@angular/material/expansion";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

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
		MatExpansionPanel,
		MatExpansionPanelHeader,
		MatExpansionPanelTitle,
	],
	standalone: true,
	styleUrl: "csv-upload.component.css"
})
export class CsvUploadComponent implements OnInit {
	file: File | null = null;
	backupFile: File | null = null;

	transactions: Transaction[] = [];
	costCenterService: CostCenterService = inject(CostCenterService);
	displayedColumns: string[] = ['bookDate','transactionDate', 'amount', 'nameOtherParty', 'costCenter'];

	private readonly baseUrl = environment.apiBaseUrl;
	private snackbar= inject(MatSnackBar);
	private horizontalPosition: MatSnackBarHorizontalPosition = 'end';
	private verticalPosition: MatSnackBarVerticalPosition = 'bottom';
	private api(path: string): string { return `${this.baseUrl}${path.startsWith('/') ? path : '/' + path}`; }

	constructor(private http: HttpClient) {}

	ngOnInit() {
		this.retrieveAllTransactions();
	}

	private retrieveAllTransactions() {
		this.http.get<Transaction[]>(this.api('/transactions/all')).subscribe({
			next: (transactions) => this.transactions = transactions,
			error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
		});
	}

	onFileSelected(event: Event) {
		const input = event.target as HTMLInputElement;
		this.file = input.files && input.files.length ? input.files[0] : null;
	}

	onBackupFileSelected(event: Event) {
		const input = event.target as HTMLInputElement;
		this.backupFile = input.files && input.files.length ? input.files[0] : null;

		if (!this.backupFile) return;

		const formData = new FormData();
		formData.append('file', this.backupFile, this.backupFile.name);

		this.http.post<Transaction[]>(this.api('/synchronization/upload'), formData, {
			reportProgress: true,
			observe: 'events'
		}).subscribe({
				next: (response: HttpEvent<Transaction[]>) => console.log(response),
				error: (error: HttpErrorResponse) => console.log(error)
			}
		);
	}

	downloadBackupFiles() {
		this.http.get(this.api('/synchronization/download'), {responseType: 'blob'})
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
		this.http.put(this.api('/synchronization/restore'), {}, {reportProgress: true, observe: 'events'})
			.subscribe({
				next: () => {
					this.retrieveAllTransactions();
					this.openSnackBar("Restore successfully", "Close", 1000);
				},
				error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
			})
	}

	assign() {
		this.http.put<Transaction[]>(this.api('/transactions/assigned'), this.transactions, {reportProgress: true, observe: 'events'})
			.subscribe({
				next: () => {
					this.retrieveAllTransactions();
					this.openSnackBar("Cost centers assigned successfully", "Close", 1000);
				},
				error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
			});
	}

	upload() {
		if (!this.file) return;

		const formData = new FormData();
		formData.append('file', this.file, this.file.name);

		this.http.post<Transaction[]>(this.api('/transactions/upload/transaction-file'), formData, {
			reportProgress: true,
			observe: 'events'
		}).subscribe({
				next: () => this.openSnackBar("Upload successful", "Close", 1000),
				error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
			}
		);
	}

	save() {
		this.http.post<any>(this.api('/synchronization/backup'), {})
			.subscribe({
				next: () => this.openSnackBar("Save successful", "Close", 1000),
				error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
			});
	}

	private openSnackBar(message: string, action: string, duration: number = 3000) {
		this.snackbar.open(message, action, {
			duration: duration,
			horizontalPosition: this.horizontalPosition,
			verticalPosition: this.verticalPosition,
		});
	}
}
