import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgIcon} from "@ng-icons/core";
import {CommonModule} from "@angular/common";
import {InputTextComponent} from "../input-text/input-text.component";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ButtonPrimaryComponent} from "../button-primary/button-primary.component";
import {Bookmark, Folder} from "../app.types";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-folder-create',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, NgIcon, InputTextComponent, ButtonPrimaryComponent],
  standalone: true,
  templateUrl: './folder-create.component.html',
  styleUrl: './folder-create.component.css'
})
export class FolderCreateComponent {
  @Input() passphrase = '';
  @Output() onSavedEvent = new EventEmitter<void>();

  isInsertMode = false;
  form: FormGroup;
  isSaving = false;
  focusEventEmitter = new EventEmitter();

  constructor(
    private fb: FormBuilder
  ) {

    this.form = this.fb.group({
      title: ['', [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50),
      ]]
    });
  }

  enableInsertMode(event: Event) {
    event.preventDefault();

    this.isInsertMode = true;

    setTimeout(() => {
      this.focusEventEmitter.emit();
    }, 0);
  }

  save() {
    const title: string = this.form.get('title')?.value || '';
    const folder: Folder = {
      title: this.form.get('title')?.value
    }

    fetch(environment.apiUrl + '/folders', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': this.passphrase
      },
      body: JSON.stringify(folder)
    })
      .then(resp => {
        this.isSaving = false;
        if (!resp.ok) {
          alert('Could not save folder.');
        }

        this.form.get('title')?.setValue('');

        this.onSavedEvent.emit();
      });
  }
}
