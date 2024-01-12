import {NgIcon, provideIcons} from '@ng-icons/core';
import {Component, Input} from '@angular/core';
import {heroArrowRightOnRectangle, heroChevronDoubleRight, heroPlus, heroTrash } from "@ng-icons/heroicons/outline";

@Component({
  selector: 'app-button-primary',
  standalone: true,
  imports: [
    NgIcon
  ],
  templateUrl: './button-primary.component.html',
  viewProviders: [provideIcons({ heroChevronDoubleRight, heroPlus, heroArrowRightOnRectangle, heroTrash })]
})
export class ButtonPrimaryComponent {
  @Input() icon = 'heroChevronDoubleRight';
  @Input() label = 'OK';
  @Input() type = 'submit';
  @Input() disabled = false;
}
