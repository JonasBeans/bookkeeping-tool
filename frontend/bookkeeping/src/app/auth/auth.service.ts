import {DOCUMENT, isPlatformBrowser} from "@angular/common";
import {inject, Injectable, PLATFORM_ID} from "@angular/core";
import Keycloak, {KeycloakProfile} from "keycloak-js";
import {BehaviorSubject} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	private readonly platformId: Object = inject(PLATFORM_ID);
	private readonly document: Document = inject(DOCUMENT);
	private readonly isBrowser = isPlatformBrowser(this.platformId);
	private readonly authenticatedSubject = new BehaviorSubject<boolean>(false);
	public readonly isAuthenticated$ = this.authenticatedSubject.asObservable();
	public userProfile: KeycloakProfile | null = null;
	private keycloak: Keycloak | null = null;
	private initPromise: Promise<boolean> | null = null;

	constructor() {
		if (this.isBrowser) {
			this.keycloak = new Keycloak(environment.keycloak);
			this.keycloak.onAuthLogout = () => this.setAuthenticated(false);
			this.keycloak.onTokenExpired = () => {
				this.keycloak?.updateToken(30).catch((error: unknown) => {
					this.setAuthenticated(false);
					console.error('Failed to refresh Keycloak token:', error);
				});
			};
		}
	}

	public initialize(): Promise<boolean> {
		if (!this.isBrowser || !this.keycloak) {
			return Promise.resolve(false);
		}
		if (this.initPromise) {
			return this.initPromise;
		}

		const initPromise = this.keycloak.init({
			onLoad: 'check-sso',
			pkceMethod: 'S256',
			silentCheckSsoRedirectUri: new URL('assets/silent-check-sso.html', this.document.baseURI).toString()
		}).then((authenticated: boolean) => {
			this.setAuthenticated(authenticated);
			if (authenticated) {
				return this.loadProfile().then(() => true);
			}
			return false;
		});
		this.initPromise = initPromise;

		return initPromise;
	}

	public isAuthenticated(): boolean {
		return this.authenticatedSubject.value;
	}

	public login(returnUrl: string = '/'): Promise<void> {
		if (!this.keycloak) {
			return Promise.resolve();
		}

		return this.keycloak.login({
			redirectUri: this.toApplicationUrl(returnUrl)
		});
	}

	public logout(): Promise<void> {
		if (!this.keycloak) {
			return Promise.resolve();
		}

		return this.keycloak.logout({
			redirectUri: new URL('', this.document.baseURI).toString()
		});
	}

	public async getToken(): Promise<string | null> {
		if (!this.keycloak || !this.keycloak.authenticated) {
			return null;
		}

		try {
			await this.keycloak.updateToken(30);
			this.setAuthenticated(true);
			return this.keycloak.token ?? null;
		} catch (error) {
			this.keycloak.clearToken();
			this.setAuthenticated(false);
			throw error;
		}
	}

	public shouldAttachToken(url: string): boolean {
		const apiBaseUrl = environment.apiBaseUrl;
		if (apiBaseUrl) {
			return url.startsWith(apiBaseUrl);
		}

		return ['/transactions', '/synchronization', '/balance', '/api']
			.some(prefix => url.startsWith(prefix));
	}

	public getDisplayName(): string {
		return this.userProfile?.firstName
			?? this.userProfile?.username
			?? this.userProfile?.email
			?? 'Signed in';
	}

	private loadProfile(): Promise<void> {
		if (!this.keycloak) {
			return Promise.resolve();
		}

		return this.keycloak.loadUserProfile()
			.then((profile: KeycloakProfile) => {
				this.userProfile = profile;
			});
	}

	private setAuthenticated(authenticated: boolean): void {
		this.authenticatedSubject.next(authenticated);
		if (!authenticated) {
			this.userProfile = null;
		}
	}

	private toApplicationUrl(returnUrl: string): string {
		const normalizedReturnUrl = returnUrl.replace(/^\/+/, '');
		return new URL(normalizedReturnUrl, this.document.baseURI).toString();
	}
}
