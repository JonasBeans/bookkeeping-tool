export interface Transaction {
	id: number
	bookDate: string;
	bookYear: number;
	transactionDate: string;
	amount: number;
	nameOtherParty: string;
	costCenterReference: string
	version: number;
}
