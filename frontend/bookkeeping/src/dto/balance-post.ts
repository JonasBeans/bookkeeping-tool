export interface GetBalancePost {
	title: string;
	subPosts: BalanceSubPost[];
}

export class AddBalancePost {
	title: string;
	subPost: BalanceSubPost;

	constructor(title: string, subPost: BalanceSubPost) {
		this.title = title;
		this.subPost = subPost;
	}
}

export interface BalanceSubPost {
	title: string;
	amount: number;
}
