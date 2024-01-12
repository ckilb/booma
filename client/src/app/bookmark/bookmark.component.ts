import {NgIcon, provideIcons} from '@ng-icons/core';
import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Bookmark} from "../app.types";
import {environment} from "../../environments/environment";
import {ButtonPrimaryComponent} from "../button-primary/button-primary.component";
import { heroTrash } from '@ng-icons/heroicons/outline';
@Component({
  selector: 'app-bookmark',
  standalone: true,
  imports: [
    ButtonPrimaryComponent,
    NgIcon,
  ],
  templateUrl: './bookmark.component.html',
  viewProviders: [provideIcons({heroTrash})]
})
export class BookmarkComponent {
  @Input() passphrase = '';
  @Input() bookmark: Bookmark = {title: '', url: ''};
  @Output() onDeletedEvent = new EventEmitter<void>();

  delete() {
    if (!window.confirm('Do you really want to delete the bookmark `' + this.bookmark.title + '`?')) {
      return;
    }

    fetch(environment.apiUrl + '/bookmarks/' + this.bookmark.id, {
      method: 'DELETE',
      headers: {
        'Authorization': this.passphrase
      }
    })
      .then(() => {
        this.onDeletedEvent.emit();
      })
  }

  protected readonly encodeURI = encodeURI;
}
