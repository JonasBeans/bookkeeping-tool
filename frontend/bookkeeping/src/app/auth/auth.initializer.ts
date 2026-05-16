import {AuthService} from "./auth.service";

export function initializeAuth(authService: AuthService): () => Promise<boolean> {
	return () => authService.initialize();
}
