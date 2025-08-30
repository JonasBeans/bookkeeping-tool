import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AddBalancePost, GetBalancePost} from "../../dto/balance-post";

@Injectable({
	providedIn: 'root'
})
export class BalanceService {

	http: HttpClient = inject(HttpClient);

	get_balance_posts(title: string) {
		return this.http.get<GetBalancePost>('http://localhost:8080/balance?title=' + title);
	}

	add_new_balance_post(add_balance_post: AddBalancePost) {
		return this.http.post<string>('http://localhost:8080/balance/add', add_balance_post, {responseType: 'text' as 'json'});
	}

	update_balance_post(edit_balance_post: AddBalancePost) {
		return this.http.put<string>('http://localhost:8080/balance/update', edit_balance_post, {responseType: 'text' as 'json'});
	}

	delete_balance_post(sub_post_title: string) {
		return this.http.delete<string>('http://localhost:8080/balance?subPostTitle=' + sub_post_title, {responseType: 'text' as 'json'});
	}

}
