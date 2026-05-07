import {isPlatformBrowser} from "@angular/common";
import {inject, Injectable, PLATFORM_ID} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {BehaviorSubject, combineLatest, map} from "rxjs";
import {environment} from "../../environments/environment";

export interface BookPeriod {
	bookYear: number | null;
	bookMonth: number | null;
}

@Injectable({
	providedIn: 'root'
})
export class BookYearService {

	public availableBookYears: number[] = [];
	public availableBookMonths: number[] = [];

	private readonly selectedBookYearSubject = new BehaviorSubject<number | null>(null);
	public readonly selectedBookYear$ = this.selectedBookYearSubject.asObservable();
	private readonly selectedBookMonthSubject = new BehaviorSubject<number | null>(null);
	public readonly selectedBookMonth$ = this.selectedBookMonthSubject.asObservable();
	private readonly client: HttpClient = inject(HttpClient);
	private readonly platformId: Object = inject(PLATFORM_ID);
	private readonly baseUrl = environment.apiBaseUrl;
	public readonly selectedBookPeriod$ = combineLatest([this.selectedBookYear$, this.selectedBookMonth$])
		.pipe(map(([bookYear, bookMonth]) => ({bookYear, bookMonth})));
	public readonly monthLabels = [
		'January',
		'February',
		'March',
		'April',
		'May',
		'June',
		'July',
		'August',
		'September',
		'October',
		'November',
		'December'
	];


	get selectedBookYear(): number | null {
		return this.selectedBookYearSubject.value;
	}

	get selectedBookMonth(): number | null {
		return this.selectedBookMonthSubject.value;
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
					} else {
						this.refreshBookMonths(this.selectedBookYear);
					}
				},
				error: error => console.error('Error fetching bookyears:', error)
			});
	}

	public refreshBookMonths(bookYear: number | null = this.selectedBookYear): void {
		if (!isPlatformBrowser(this.platformId)) {
			return;
		}
		if (bookYear === null) {
			this.availableBookMonths = [];
			this.selectBookMonth(null);
			return;
		}
		this.client.get<number[]>(this.api('/transactions/bookmonths'), {
			params: new HttpParams().set('bookYear', bookYear)
		}).subscribe({
			next: bookMonths => {
				this.availableBookMonths = bookMonths;
				if (this.selectedBookMonth !== null && !bookMonths.includes(this.selectedBookMonth)) {
					this.selectBookMonth(null);
				}
			},
			error: error => console.error('Error fetching bookmonths:', error)
		});
	}

	public selectBookYear(bookYear: number | null): void {
		if (this.selectedBookYear !== bookYear) {
			this.selectedBookYearSubject.next(bookYear);
		}
		this.refreshBookMonths(bookYear);
	}

	public selectBookMonth(bookMonth: number | null): void {
		if (this.selectedBookMonth !== bookMonth) {
			this.selectedBookMonthSubject.next(bookMonth);
		}
	}

	public getBookMonthLabel(bookMonth: number): string {
		return this.monthLabels[bookMonth - 1] ?? bookMonth.toString();
	}

	private api(path: string): string {
		return `${this.baseUrl}${path.startsWith('/') ? path : '/' + path}`;
	}
}
