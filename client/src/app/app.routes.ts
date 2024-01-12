import { Routes } from '@angular/router';
import {BookmarksComponent} from "./bookmarks/bookmarks.component";
import {LoginComponent} from "./login/login.component";

export const routes: Routes = [
  { path: 'bookmarks/:passphrase', component: BookmarksComponent },
  { path: '**', component: LoginComponent },
];
