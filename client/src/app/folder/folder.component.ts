import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgIcon, provideIcons} from "@ng-icons/core";
import {Folder} from "../app.types";
import {heroFolder, heroTrash} from '@ng-icons/heroicons/outline';
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-folder',
  standalone: true,
  imports: [
      NgIcon, RouterModule
  ],
  templateUrl: './folder.component.html',
  styleUrl: './folder.component.css',
  viewProviders: [provideIcons({heroFolder, heroTrash})]
})
export class FolderComponent {
  @Input() passphrase = '';
  @Input() folder: Folder = {title: '', path: ''};
  @Output() onDeletedEvent = new EventEmitter<void>();

  delete() {
    this.onDeletedEvent.emit();
  }
}
