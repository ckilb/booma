import {NgIcon, provideIcons} from '@ng-icons/core';
import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router, RouterModule} from "@angular/router";
import {environment} from "../../environments/environment";
import {Entry} from "../app.types";
import {CommonModule} from "@angular/common";
import {BookmarkCreateComponent} from "../bookmark-create/bookmark-create.component";
import {BookmarkComponent} from "../bookmark/bookmark.component";
import {InputTextComponent} from "../input-text/input-text.component";
import {ButtonPrimaryComponent} from "../button-primary/button-primary.component";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LoadingIconComponent} from "../loading-icon/loading-icon.component";
import {heroChevronDoubleLeft, heroFolderPlus} from "@ng-icons/heroicons/outline";
import {FolderComponent} from "../folder/folder.component";
import {FolderCreateComponent} from "../folder-create/folder-create.component";

@Component({
  selector: 'app-entries',
  standalone: true,
  imports: [CommonModule, BookmarkCreateComponent, BookmarkComponent, InputTextComponent, ButtonPrimaryComponent, LoadingIconComponent, NgIcon, FolderComponent, FolderCreateComponent, RouterModule],
  templateUrl: './entries.component.html',
  encapsulation: ViewEncapsulation.None,
  viewProviders: [provideIcons({ heroFolderPlus, heroChevronDoubleLeft })]
})
export class EntriesComponent {
  @Input() passphrase: string = '';
  @Input() folder = '';
  entries: Entry[] = [];
  isBookmarksLoading = true;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    route: ActivatedRoute
  ) {
    this.passphrase = route.snapshot.params['passphrase'];
    this.folder = route.snapshot.params['folder'];
    this.form = this.fb.group({
      passphrase: [this.passphrase],
    });

    this.form.get('passphrase')?.disable();

    this.loadEntries();
  }

  deleteEntry(entryId: number) {
    if (!window.confirm('Do you really want to delete this entry?')) {
      return;
    }

    fetch(environment.apiUrl + '/entries/' + entryId, {
      method: 'DELETE',
      headers: {
        'Authorization': this.passphrase
      }
    })
      .then(() => {
        this.loadEntries();
      })
  }

  loadEntries() {
    this.isBookmarksLoading = true;

    let url = environment.apiUrl + '/entries';

    if (this.folder) {
      url = url +  '?folder=' + this.folder;
    }

    fetch(url, {
      headers: {
        'Authorization': this.passphrase
      }
    })
      .then(resp => resp.json())
      .then((entries: Entry[]) => {
        this.entries = entries;
        this.isBookmarksLoading = false;
      })
  }

  logout() {
    void this.router.navigate(['/']);
  }

    protected readonly encodeURI = encodeURI;
}
