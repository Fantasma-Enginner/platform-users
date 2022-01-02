import { APP_BASE_HREF } from '@angular/common';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EmptyRouteComponent } from './empty-route/empty-route.component';
import { ProfileListComponent } from './profile-list/profile-list.component';
import { ProfileEditComponent } from './profile-edit/profile-edit.component';
import { UserEditComponent } from './user-edit/user-edit.component';
import { UserListComponent } from './user-list/user-list.component';
import { AuthGuard } from './services/auth-guard.service';

const routes: Routes = [
  { path: 'users-app/users', component: UserListComponent, canActivate: [AuthGuard] },
  { path: 'users-app/user-create', component: UserEditComponent, canActivate: [AuthGuard] },
  { path: 'users-app/user-edit/:id', component: UserEditComponent, canActivate: [AuthGuard] },
  { path: 'users-app/profiles', component: ProfileListComponent, canActivate: [AuthGuard] },
  { path: 'users-app/profiles/create', component: ProfileEditComponent, canActivate: [AuthGuard] },
  { path: 'users-app/profiles/edit/:id', component: ProfileEditComponent, canActivate: [AuthGuard] },
  { path: '**', component: EmptyRouteComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [
    { provide: APP_BASE_HREF, useValue: '/' }
  ],
})
export class AppRoutingModule { }
