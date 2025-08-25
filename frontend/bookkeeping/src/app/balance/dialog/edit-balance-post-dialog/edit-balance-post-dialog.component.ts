import {Component, inject, model} from '@angular/core';
import {
	MAT_DIALOG_DATA,
	MatDialogActions,
	MatDialogClose,
	MatDialogContent,
	MatDialogRef
} from "@angular/material/dialog";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BalanceService} from "../../../services/balance.service";

@Component({
  selector: 'app-edit-balance-post-dialog',
  standalone: true,
	imports: [
		MatDialogActions,
		MatDialogContent,
		MatFormField,
		MatInput,
		MatLabel,
		ReactiveFormsModule,
		FormsModule,
		MatDialogClose
	],
  templateUrl: './edit-balance-post-dialog.component.html',
  styleUrl: './edit-balance-post-dialog.component.css'
})
export class EditBalancePostDialogComponent {
	readonly dialogRef = inject(MatDialogRef<EditBalancePostDialogComponent>);
	readonly data: EditBalancePostDialogData = inject<EditBalancePostDialogData>(MAT_DIALOG_DATA);
	balanceService = inject(BalanceService);
	readonly balance_post_title = this.data.balance_post_title;
	readonly edit_balance_post_amount = model(this.data.edit_balance_post_amount);
	readonly edit_balance_post_title = model(this.data.edit_balance_post_title);

	onNoClick() {
		this.dialogRef.close();
	}

	delete_sub_post() {
		this.balanceService.delete_balance_post(this.balance_post_title, this.edit_balance_post_title())
			.subscribe({
				next: resonse => {console.log(resonse); this.dialogRef.close('deleted')},
				error : error => console.error(error),
			})
	}
}

export interface EditBalancePostDialogData {
	balance_post_title: string;
	edit_balance_post_title: string;
	edit_balance_post_amount: number;
}
