import {Component, inject} from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpEventType } from '@angular/common/http';
import {NgIf} from "@angular/common";
import {response} from "express";
import {Transaction} from "../../../dto/transaction";
import {CostCenterService} from "../../services/cost-center.service";
import {CostCenterComponent} from "../../cost_centers/cost-center/cost-center.component";


@Component({
  selector: 'app-csv-upload',
  templateUrl: './csv-upload.component.html',
	imports: [
		NgIf,
		CostCenterComponent
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

  constructor(private http: HttpClient) {}

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
      next: event => {
        if (event.type === HttpEventType.UploadProgress && event.total) {
          this.progress = Math.round((event.loaded / event.total) * 100);
        } else if (event.type === HttpEventType.Response) {
          this.progress = 100;
          this.uploading = false;
        }
        this.loadTransaction();
        console.log(this.transactions);
      },
      error: (err: HttpErrorResponse) => {
        this.error = err.message;
        this.uploading = false;
        this.progress = -1;
      }
    });
  }

   loadTransaction() {
     this.http.get<Transaction[]>("http://localhost:8080/transactions/all").subscribe((response) => { this.transactions = response } )
  }

}
