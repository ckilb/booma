import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {environment} from "../../environments/environment";
import {Bookmark} from "../app.types";
import {CommonModule} from "@angular/common";
import {BookmarkCreateComponent} from "../bookmark-create/bookmark-create.component";
import {BookmarkComponent} from "../bookmark/bookmark.component";
import {InputTextComponent} from "../input-text/input-text.component";
import {ButtonPrimaryComponent} from "../button-primary/button-primary.component";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LoadingIconComponent} from "../loading-icon/loading-icon.component";

@Component({
  selector: 'app-bookmarks',
  standalone: true,
  imports: [CommonModule, BookmarkCreateComponent, BookmarkComponent, InputTextComponent, ButtonPrimaryComponent, LoadingIconComponent],
  templateUrl: './bookmarks.component.html',
  encapsulation: ViewEncapsulation.None
})
export class BookmarksComponent {
  @Input() passphrase: string = '';
  bookmarks: Bookmark[] = [];
  isBookmarksLoading = true;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    route: ActivatedRoute
  ) {
    this.passphrase = route.snapshot.params['passphrase'];
    this.form = this.fb.group({
      passphrase: [this.passphrase],
    });

    this.form.get('passphrase')?.disable();

    this.loadBookmarks();
  }

  loadBookmarks() {
    this.isBookmarksLoading = true;
    fetch(environment.apiUrl + '/bookmarks', {
      headers: {
        'Authorization': this.passphrase
      }
    })
      .then(resp => resp.json())
      .then(bookmarks => {
        this.bookmarks = bookmarks;
        this.isBookmarksLoading = false;
      })
  }

  logout() {
    void this.router.navigate(['/']);
  }
}
