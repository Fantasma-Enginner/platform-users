import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { PlatformLocation } from '@angular/common';

import { WebcamModule } from 'ngx-webcam';

import { ApiModule } from '@tsir/users-api';
import { BASE_PATH as USERS_PATH } from '@tsir/users-api';
import { BASE_PATH as TOLLS_PATH } from '@tsir/tolls-api';
import { BASE_PATH as RESOURCES_PATH } from '@tsir/resources-api';

import { AppComponent } from './app.component';

import { EmptyRouteComponent } from './empty-route/empty-route.component';
import { UserEditComponent } from './user-edit/user-edit.component';
import { UserListComponent } from './user-list/user-list.component';
import { ProfileListComponent } from './profile-list/profile-list.component';
import { ProfileEditComponent } from './profile-edit/profile-edit.component';
import { AuthorizationComponent } from './authorization/authorization.component';
import { HttpErrorInterceptor } from './helpers/http-error-interceptor';

import { environment } from 'src/environments/environment';
import { PaginationComponent } from './pagination/pagination.component';
import { JwtModule } from '@auth0/angular-jwt';

const portGateway = 8760;

export function getResourcesURL(pl: PlatformLocation) {
  const protocol = environment.production ? pl.protocol : 'http:';
  return `${protocol}//${pl.hostname}:${portGateway}/platform-manager/api/v1`;
}

export function getUsersURL(pl: PlatformLocation) {
  const protocol = environment.production ? pl.protocol : 'http:';
  return `${protocol}//${pl.hostname}:${portGateway}/settings-users/api/v1`;
}

export function getTollsURL(pl: PlatformLocation) {
  const protocol = environment.production ? pl.protocol : 'http:';
  return `${protocol}//${pl.hostname}:${portGateway}/tolls-manager/api/v1`;
}

export function tokenGetter() {
  return localStorage.getItem("token");
}

@NgModule({
  declarations: [
    AppComponent,
    EmptyRouteComponent,
    ProfileEditComponent,
    ProfileListComponent,
    UserEditComponent,
    UserListComponent,
    AuthorizationComponent,
    PaginationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ApiModule,
    HttpClientModule,
    ReactiveFormsModule,
    WebcamModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter
      }
    }),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true
    },
    {
      provide: USERS_PATH, useFactory: getUsersURL,
      deps: [PlatformLocation]
    },
    {
      provide: RESOURCES_PATH, useFactory: getResourcesURL,
      deps: [PlatformLocation]
    },
    {
      provide: TOLLS_PATH, useFactory: getTollsURL,
      deps: [PlatformLocation]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

