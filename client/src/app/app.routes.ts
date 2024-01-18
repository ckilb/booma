import { Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {EntriesComponent} from "./entries/entries.component";

export const routes: Routes = [
  { path: 'bookmarks/:passphrase/:folder', component: EntriesComponent },
  { path: 'bookmarks/:passphrase', component: EntriesComponent },
  { path: '**', component: LoginComponent },
];
