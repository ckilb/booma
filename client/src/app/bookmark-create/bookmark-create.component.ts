import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";
import {Bookmark} from "../app.types";
import {environment} from "../../environments/environment";
import {InputTextComponent} from "../input-text/input-text.component";
import {ButtonPrimaryComponent} from "../button-primary/button-primary.component";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-bookmark-create',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, InputTextComponent, ButtonPrimaryComponent],
  templateUrl: './bookmark-create.component.html',
})
export class BookmarkCreateComponent {
  @Input() passphrase = '';
  @Output() onSavedEvent = new EventEmitter<void>();

  form: FormGroup;
  isCustomTitleMode = false;
  isSaving = false;

  constructor(
    private fb: FormBuilder
  ) {
    const urlPattern = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';

    this.form = this.fb.group({
      title: ['', [
        Validators.required,
        Validators.minLength(4),
      ]],
      url:['', [
        Validators.required,
        Validators.minLength(4),
        Validators.pattern(urlPattern)
      ]]
    });

    this.enableTitleGeneration();
  }

  enableTitleGeneration() {
    let previousTitle = '';

    this.form.valueChanges.subscribe((data) => {
      const url: string = this.form.get('url')?.value || '';

      if (url.length < 1) {
        return;
      }

      const titleCtrl = this.form.get('title');

      if (titleCtrl?.value != previousTitle) {
        return;
      }

      const title = url.replace(/^https?:\/\//, '');

      if (title == titleCtrl?.value) {
        return;
      }

      titleCtrl?.setValue(url.replace(/^https?:\/\//, ''));

      previousTitle = title;
    })
  }

  save() {
    this.isSaving = true;

    const bookmark: Bookmark = {
      title: this.form.get('title')?.value,
      url: this.form.get('url')?.value,
    }

    fetch(environment.apiUrl + '/bookmarks', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': this.passphrase
      },
      body: JSON.stringify(bookmark)
    })
      .then(resp => {
        this.isSaving = false;
        if (!resp.ok) {
          alert('Could not save bookmark.');
        }

        return resp.json()
      })
      .then(() => {
        this.form.get('title')?.setValue('');
        this.form.get('url')?.setValue('');
        this.isCustomTitleMode = false;

        this.onSavedEvent.emit();
      });
  }
}
