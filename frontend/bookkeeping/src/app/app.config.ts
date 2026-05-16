import {APP_INITIALIZER, ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideClientHydration} from '@angular/platform-browser';
import {provideHttpClient, withFetch, withInterceptors} from "@angular/common/http";
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {authInterceptor} from "./auth/auth.interceptor";
import {initializeAuth} from "./auth/auth.initializer";
import {AuthService} from "./auth/auth.service";

export const appConfig: ApplicationConfig = {
  providers: [
	  provideRouter(routes),
	  provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
	  provideClientHydration(),
	  provideAnimationsAsync(),
	  {
		  provide: APP_INITIALIZER,
		  useFactory: initializeAuth,
		  deps: [AuthService],
		  multi: true
	  }
 ]
};
