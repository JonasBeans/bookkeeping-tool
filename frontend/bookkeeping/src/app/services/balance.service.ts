import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AddBalancePost, GetBalancePost} from "../../dto/balance-post";
import {environment} from "../../environments/environment";

@Injectable({
	providedIn: 'root'
})
export class BalanceService {

	http: HttpClient = inject(HttpClient);
	private readonly baseUrl = environment.apiBaseUrl;
	private api(path: string): string { return `${this.baseUrl}${path.startsWith('/') ? path : '/' + path}`; }

	get_balance_posts(title: string) {
		return this.http.get<GetBalancePost>(this.api('/balance?title=' + encodeURIComponent(title)));
	}

	add_new_balance_post(add_balance_post: AddBalancePost) {
		return this.http.post<string>(this.api('/balance/add'), add_balance_post, {responseType: 'text' as 'json'});
	}

	update_balance_post(edit_balance_post: AddBalancePost) {
		return this.http.put<string>(this.api('/balance/update'), edit_balance_post, {responseType: 'text' as 'json'});
	}

	delete_balance_post(sub_post_title: string) {
		return this.http.delete<string>(this.api('/balance?subPostTitle=' + encodeURIComponent(sub_post_title)), {responseType: 'text' as 'json'});
	}

}
