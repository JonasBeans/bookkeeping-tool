import {Component, Input} from '@angular/core';
import {CurrencyPipe, NgForOf} from "@angular/common";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-balance-post',
  standalone: true,
	imports: [
		CurrencyPipe,
		FormsModule,
		NgForOf
	],
  templateUrl: './balance-post.component.html',
  styleUrl: './balance-post.component.css'
})
export class BalancePostComponent {
	@Input() post_title: string = "";
	sub_posts: SubPost[] = [];

	total_post_amount: number = 0;
	sub_post_amount: number = 0;
	sub_posts_title: string = "";

	add_sub_post(sub_post_title: string,sub_post_amount: number) {
		this.sub_posts.push({title: sub_post_title, amount: sub_post_amount});
	}
}

export interface SubPost {
	title: string;
	amount: number;
}
