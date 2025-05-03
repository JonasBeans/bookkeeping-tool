import {Component, inject} from '@angular/core';
import {Transaction} from "../../../dto/transaction";
import {HttpClient, HttpErrorResponse, HttpEvent, HttpEventType} from "@angular/common/http";

@Component({
	selector: 'app-balance-sheet',
	standalone: true,
	imports: [],
	templateUrl: './balance-sheet.component.html',
	styleUrl: './balance-sheet.component.css'
})
export class BalanceSheetComponent {

	private http: HttpClient = inject(HttpClient);

	file: File | null = null;

	progress = -1;
	uploading = false;
	error: string | null = null;

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

		this.http.post<Transaction[]>('http://localhost:8080/balance-sheet/', formData, {
			reportProgress: true,
			observe: 'events'
		}).subscribe({
				next: (response: HttpEvent<Transaction[]>) => this.upload_callback(response),
				error: (error: HttpErrorResponse) => this.handle_upload_error(error)
			}
		);
	}

	upload_callback(event: HttpEvent<Transaction[]>) {
		console.log(event.type)
		if (event.type === HttpEventType.UploadProgress && event.total) {
			this.progress = Math.round((event.loaded / event.total) * 100);
		} else if (event.type === HttpEventType.Response) {
			this.progress = 100;
			this.uploading = false;
		}
	}

	handle_upload_error(error: HttpErrorResponse) {
		this.error = error.message;
		this.uploading = false;
		this.progress = -1;
	}

}
