export interface Transaction {
	id: number
	bookDate: string;
	transactionDate: string;
	amount: number;
	nameOtherParty: string;
	costCenterReference: string
	version: number;
}
