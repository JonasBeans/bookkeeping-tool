export interface Transaction {
	id: number
	bookDate: string;
	bookYear: number;
	transactionDate: string;
	amount: number;
	description: string;
	nameOtherParty: string;
	message: string;
	costCenterReference: string
	version: number;
}
