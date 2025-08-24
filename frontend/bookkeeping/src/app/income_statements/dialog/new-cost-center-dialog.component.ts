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
import {MatCheckbox} from "@angular/material/checkbox";
import {MatInputModule} from "@angular/material/input";

@Component({
  selector: 'app-dialog',
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
		MatCheckbox
	],
  templateUrl: './new-cost-center-dialog.component.html',
  styleUrl: './new-cost-center-dialog.component.css'
})
export class NewCostCenterDialog {
	readonly dialogRef = inject(MatDialogRef<NewCostCenterDialog>);
	readonly data = inject<NewCostCenterDialogData>(MAT_DIALOG_DATA);
	readonly new_cost_center_title = model(this.data.new_cost_center_title);
	readonly is_new_cost_center_a_cost = model(this.data.is_new_cost_center_a_cost);

	onNoClick() {
		this.dialogRef.close();
	}
}

export interface NewCostCenterDialogData {
	new_cost_center_title: string;
	is_new_cost_center_a_cost: boolean;
}
