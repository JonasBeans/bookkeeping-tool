import {Component, inject, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {BookYearService} from "./services/book-year.service";
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOption, MatSelectModule} from "@angular/material/select";

@Component({
  selector: 'app-root',
  standalone: true,
	imports: [RouterOutlet, RouterLink, FormsModule, NgForOf, NgIf, MatFormFieldModule, MatSelectModule, MatOption],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'bookkeeping';
	bookYearService: BookYearService = inject(BookYearService);

	ngOnInit(): void {
		this.bookYearService.refreshBookYears();
	}
}
