import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private TOKEN_NAME = 'token';

  constructor(
    private jwtHelper: JwtHelperService
  ) { }

  startSession(token: string) {
    localStorage.setItem(this.TOKEN_NAME, token);
  }

  stopSession() {
    localStorage.removeItem(this.TOKEN_NAME);
  }

  isLogged(): boolean {
    return !this.jwtHelper.isTokenExpired();
  }
}
