import {CommonModule} from "@angular/common";
import {Component, inject} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../auth/auth.service";

@Component({
	selector: 'app-login',
	standalone: true,
	imports: [CommonModule],
	templateUrl: './login.component.html',
	styleUrl: './login.component.css'
})
export class LoginComponent {

	private readonly route: ActivatedRoute = inject(ActivatedRoute);
	public readonly authService: AuthService = inject(AuthService);

	public login(): void {
		this.authService.login(this.route.snapshot.queryParamMap.get('returnUrl') ?? '/');
	}
}
