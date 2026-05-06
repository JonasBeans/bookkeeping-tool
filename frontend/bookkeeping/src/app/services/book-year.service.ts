import {isPlatformBrowser} from "@angular/common";
import {inject, Injectable, PLATFORM_ID} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
	providedIn: 'root'
})
export class BookYearService {

	public availableBookYears: number[] = [];
	private readonly selectedBookYearSubject = new BehaviorSubject<number | null>(null);
	public readonly selectedBookYear$ = this.selectedBookYearSubject.asObservable();
	private readonly client: HttpClient = inject(HttpClient);
	private readonly platformId: Object = inject(PLATFORM_ID);
	private readonly baseUrl = environment.apiBaseUrl;

	get selectedBookYear(): number | null {
		return this.selectedBookYearSubject.value;
	}

	public refreshBookYears(): void {
		if (!isPlatformBrowser(this.platformId)) {
			return;
		}
		this.client.get<number[]>(this.api('/transactions/bookyears'))
			.subscribe({
				next: bookYears => {
					this.availableBookYears = bookYears;
					if (bookYears.length === 0) {
						this.selectBookYear(null);
						return;
					}
					if (this.selectedBookYear === null || !bookYears.includes(this.selectedBookYear)) {
						this.selectBookYear(bookYears[0]);
					}
				},
				error: error => console.error('Error fetching bookyears:', error)
			});
	}

	public selectBookYear(bookYear: number | null): void {
		if (this.selectedBookYear !== bookYear) {
			this.selectedBookYearSubject.next(bookYear);
		}
	}

	private api(path: string): string {
		return `${this.baseUrl}${path.startsWith('/') ? path : '/' + path}`;
	}
}
