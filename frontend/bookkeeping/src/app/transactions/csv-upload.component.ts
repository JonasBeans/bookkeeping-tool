import {Component, inject, OnDestroy, OnInit, PLATFORM_ID} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpEventType, HttpParams} from '@angular/common/http';
import {DatePipe, isPlatformBrowser, NgForOf, NgIf} from "@angular/common";
import {Transaction} from "../../dto/transaction";
import {CostCenterService} from "../services/cost-center.service";
import {MatTableModule} from "@angular/material/table";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOption, MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";
import {environment} from "../../environments/environment";
import {MatExpansionPanel, MatExpansionPanelHeader, MatExpansionPanelTitle} from "@angular/material/expansion";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {BookYearService} from "../services/book-year.service";
import {Subscription} from "rxjs";

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
export class CsvUploadComponent implements OnInit, OnDestroy {
	file: File | null = null;
	backupFile: File | null = null;

	transactions: Transaction[] = [];
	costCenterService: CostCenterService = inject(CostCenterService);
	bookYearService: BookYearService = inject(BookYearService);
	displayedColumns: string[] = ['bookYear', 'bookDate', 'transactionDate', 'amount', 'description', 'nameOtherParty', 'message', 'costCenter'];
	private readonly subscriptions = new Subscription();
	private readonly platformId: Object = inject(PLATFORM_ID);

	private readonly baseUrl = environment.apiBaseUrl;
	private snackbar = inject(MatSnackBar);
	private horizontalPosition: MatSnackBarHorizontalPosition = 'end';
	private verticalPosition: MatSnackBarVerticalPosition = 'bottom';

	constructor(private http: HttpClient) {
	}

	ngOnInit() {
		this.subscriptions.add(this.bookYearService.selectedBookPeriod$
			.subscribe(period => this.retrieveAllTransactions(period.bookYear, period.bookMonth)));
	}

	assign() {
		this.http.put<Transaction[]>(this.api('/transactions/assigned'), this.transactions, {
			...this.createRequestOptions(this.bookYearService.selectedBookYear, this.bookYearService.selectedBookMonth),
			reportProgress: true,
			observe: 'events'
		})
			.subscribe({
				next: (response: HttpEvent<Transaction[]>) => this.assign_callback(response),
				error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
			});
	}

	ngOnDestroy(): void {
		this.subscriptions.unsubscribe();
	}

	restore() {
		this.http.put(this.api('/synchronization/restore'), {}, {reportProgress: true, observe: 'events'})
			.subscribe({
				next: () => {
					this.bookYearService.refreshBookYears();
					this.retrieveAllTransactions();
					this.openSnackBar("Restore successfully", "Close", 1000);
				},
				error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
			})
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

	deleteSelectedBookYearTransactions() {
		const selectedBookYear = this.bookYearService.selectedBookYear;
		if (selectedBookYear === null) {
			return;
		}
		if (!window.confirm(`Delete all transactions for bookyear ${selectedBookYear}?`)) {
			return;
		}

		this.http.delete<void>(this.api(`/transactions/bookyears/${selectedBookYear}`))
			.subscribe({
				next: () => {
					this.transactions = [];
					this.bookYearService.refreshBookYears();
					this.costCenterService.refresh_data(selectedBookYear);
					this.openSnackBar(`Transactions for ${selectedBookYear} deleted`, "Close", 3000);
				},
				error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
			});
	}

	private api(path: string): string {
		return `${this.baseUrl}${path.startsWith('/') ? path : '/' + path}`;
	}

	upload() {
		if (!this.file) return;

		const formData = new FormData();
		formData.append('file', this.file, this.file.name);

		this.http.post<Transaction[]>(this.api('/transactions/upload/transaction-file'), formData, {
			reportProgress: true,
			observe: 'events'
		}).subscribe({
			next: (response: HttpEvent<Transaction[]>) => this.upload_callback(response),
				error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
			}
		);
	}

	private retrieveAllTransactions(bookYear: number | null = this.bookYearService.selectedBookYear,
	                                bookMonth: number | null = this.bookYearService.selectedBookMonth) {
		if (!isPlatformBrowser(this.platformId)) {
			return;
		}
		this.http.get<Transaction[]>(this.api('/transactions/all'), this.createRequestOptions(bookYear, bookMonth)).subscribe({
			next: (transactions) => this.transactions = transactions,
			error: (error: HttpErrorResponse) => this.snackbar.open(`Error: ${error}`, "Close")
		});
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

	private upload_callback(event: HttpEvent<Transaction[]>): void {
		if (event.type === HttpEventType.Response) {
			this.transactions = event.body || [];
			const uploadedBookYears = this.getBookYears(this.transactions);
			this.bookYearService.refreshBookYears();
			if (uploadedBookYears.length > 0) {
				this.bookYearService.selectBookYear(uploadedBookYears[0]);
			}
			this.openSnackBar("Upload successful", "Close", 1000);
		}
	}

	private assign_callback(event: HttpEvent<Transaction[]>): void {
		if (event.type === HttpEventType.Response) {
			this.transactions = event.body || [];
			this.costCenterService.refresh_data(this.bookYearService.selectedBookYear, this.bookYearService.selectedBookMonth);
			this.openSnackBar("Cost centers assigned successfully", "Close", 1000);
		}
	}

	private createRequestOptions(bookYear: number | null, bookMonth: number | null): { params?: HttpParams } {
		if (bookYear === null) {
			return {};
		}
		let params = new HttpParams().set('bookYear', bookYear);
		if (bookMonth !== null) {
			params = params.set('bookMonth', bookMonth);
		}
		return {params};
	}

	private getBookYears(transactions: Transaction[]): number[] {
		return [...new Set(transactions
			.map(transaction => transaction.bookYear ?? new Date(transaction.bookDate).getFullYear()))]
			.sort((first, second) => second - first);
	}
}
