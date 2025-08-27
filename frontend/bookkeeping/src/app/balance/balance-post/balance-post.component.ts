import {Component, inject, Input, OnInit, signal} from '@angular/core';
import {CurrencyPipe, NgForOf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";
import {AddBalancePostDialogComponent} from "../dialog/add-balance-post-dialog/add-balance-post-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {BalanceService} from "../../services/balance.service";
import {AddBalancePost} from "../../../dto/balance-post";
import {EditBalancePostDialogComponent} from "../dialog/edit-balance-post-dialog/edit-balance-post-dialog.component";

@Component({
	selector: 'app-balance-post',
	standalone: true,
	imports: [
		CurrencyPipe,
		FormsModule,
		NgForOf,
		MatIcon
	],
	templateUrl: './balance-post.component.html',
	styleUrl: './balance-post.component.css'
})
export class BalancePostComponent implements OnInit {

	@Input() post_title: string = "";
	sub_posts = signal<SubPost[]>([]);
	balance_service = inject(BalanceService);

	readonly dialog = inject(MatDialog);

	total_post_amount: number = 0;
	readonly new_balance_post_title = signal('');
	readonly new_balance_post_amount = signal(0.0);

	ngOnInit(): void {
		this.get_balance_post(this.post_title);
	}

	get_balance_post(title: string): void {
		this.balance_service.get_balance_posts(title).subscribe({
			next: result => {
				this.sub_posts.update(posts => [...result.subPosts])
				this.sub_posts().forEach(post => this.total_post_amount += post.amount);
			},
			error: error => {
				console.log(error)
			}
		})
	}

	add_sub_post(sub_post_title: string, sub_post_amount: number) {
		//this.sub_posts.push({title: sub_post_title, amount: sub_post_amount});
		this.balance_service.add_new_balance_post(new AddBalancePost(this.post_title, {
			title: sub_post_title,
			amount: sub_post_amount
		}))
			.subscribe({
				next: result => {
					console.log(result);
					this.get_balance_post(this.post_title);
				},
				error: error => {
					console.log(error)
				}
			});
	}

	update_sub_post(sub_post_title: string, sub_post_amount: number) {
		console.log(sub_post_title)
		console.log(sub_post_amount)
		this.balance_service.update_balance_post(new AddBalancePost(this.post_title, {
			title: sub_post_title,
			amount: sub_post_amount
		}))
			.subscribe({
				next: result => {
					console.log(result);
					this.get_balance_post(this.post_title);
				},
				error: error => {
					console.log(error)
				}
			});
	}

	open_dialog_add_sub_post() {
		const dialogref = this.dialog.open(AddBalancePostDialogComponent,
			{
				data: {
					new_balance_post_title: this.new_balance_post_title(),
					new_balance_post_amount: this.new_balance_post_amount()
				}
			}
		);
		dialogref.afterClosed().subscribe(result => {
			console.log('The dialog was closed');
			this.new_balance_post_title.set(result.new_balance_post_title);
			this.new_balance_post_amount.set(result.new_balance_post_amount);
			console.log(result)
			console.log(this.new_balance_post_title())
			console.log(result.new_balance_post_title);
			this.add_sub_post(this.new_balance_post_title(), this.new_balance_post_amount());
		});
	}

	open_dialog_edit_sub_post(title: string, amount: number) {
		const dialogref = this.dialog.open(EditBalancePostDialogComponent,
			{
				data: {
					balance_post_title: this.post_title,
					edit_balance_post_title: title,
					edit_balance_post_amount: amount
				}
			}
		);
		dialogref.afterClosed().subscribe(result => {
			this.update_sub_post(result.edit_balance_post_title, result.edit_balance_post_amount);
		});
	}

}

export interface SubPost {
	title: string;
	amount: number;
}
