import {Component, inject, model} from '@angular/core';
import {
	MAT_DIALOG_DATA,
	MatDialogActions,
	MatDialogClose,
	MatDialogContent,
	MatDialogRef
} from "@angular/material/dialog";
import {MatFormField, MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {FormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";

@Component({
	selector: 'app-add-balance-post-dialog',
	standalone: true,
	imports: [
		MatDialogContent,
		MatFormField,
		MatLabel,
		FormsModule,
		MatDialogActions,
		MatDialogClose,
		MatInputModule,
		MatFormFieldModule,
	],
	templateUrl: './add-balance-post-dialog.component.html',
	styleUrl: './add-balance-post-dialog.component.css'
})
export class AddBalancePostDialogComponent {
	readonly dialogRef = inject(MatDialogRef<AddBalancePostDialogComponent>);
	readonly data: AddBalancePostDialogData = inject<AddBalancePostDialogData>(MAT_DIALOG_DATA);
	readonly new_balance_post_amount = model(this.data.new_balance_post_amount);
	readonly new_balance_post_title = model(this.data.new_balance_post_title);

	onNoClick() {
		this.dialogRef.close();
	}

}

export interface AddBalancePostDialogData {
	new_balance_post_title: string;
	new_balance_post_amount: number;
}
