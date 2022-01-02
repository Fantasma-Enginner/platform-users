import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivate } from '@angular/router';
import { SessionService } from './session.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(private auth: SessionService, private router: Router) { }

    canActivate() {
        if (this.auth.isLogged()) {
            return true;
        } else {
            this.router.navigate(['/vial-plus']);
            return false;
        }
    }
}