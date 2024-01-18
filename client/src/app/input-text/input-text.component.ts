import { provideIcons } from '@ng-icons/core';
import {Component, ElementRef, EventEmitter, Input, ViewChild} from '@angular/core';
import {CommonModule} from "@angular/common";
import {FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgIcon} from "@ng-icons/core";
import {heroChatBubbleBottomCenterText, heroKey, heroLink} from "@ng-icons/heroicons/outline";
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-input-text',
  standalone: true,
  imports: [CommonModule, FormsModule, NgIcon, ReactiveFormsModule],
  templateUrl: './input-text.component.html',
  viewProviders: [provideIcons({ heroKey, heroLink, heroChatBubbleBottomCenterText })]
})
export class InputTextComponent {
  @Input() formGroup: FormGroup = new FormGroup({});
  @Input() name = '';
  @Input() type = 'text';
  @Input() icon = 'lucideSubtitles';
  @Input() placeholder = '';
  @Input() minlength = 0;
  @Input() maxlength = 512;
  @Input() focus: EventEmitter<void> | undefined;
  @ViewChild('inputElement') inputElement: ElementRef<HTMLInputElement> | undefined;

  id = uuidv4();

  ngOnInit() {
    if (this.focus) {
      this.focus.subscribe(() => {
        if (this.inputElement) {
          this.inputElement.nativeElement.focus();
        }
      });
    }
  }
}
