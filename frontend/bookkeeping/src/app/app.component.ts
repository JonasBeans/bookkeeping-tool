import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {BookYearService} from "./services/book-year.service";
import {FormsModule} from "@angular/forms";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOption, MatSelectModule} from "@angular/material/select";
import {Subscription} from "rxjs";
import {AuthService} from "./auth/auth.service";

@Component({
  selector: 'app-root',
  standalone: true,
	imports: [RouterOutlet, RouterLink, FormsModule, NgForOf, NgIf, AsyncPipe, MatFormFieldModule, MatSelectModule, MatOption],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'bookkeeping';
	bookYearService: BookYearService = inject(BookYearService);
	authService: AuthService = inject(AuthService);
	private readonly subscriptions = new Subscription();

	ngOnInit(): void {
		this.subscriptions.add(this.authService.isAuthenticated$
			.subscribe(isAuthenticated => {
				if (isAuthenticated) {
					this.bookYearService.refreshBookYears();
				}
			}));
	}

	ngOnDestroy(): void {
		this.subscriptions.unsubscribe();
	}

	logout(): void {
		this.authService.logout();
	}
}
