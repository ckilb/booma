import {NgIcon, provideIcons} from '@ng-icons/core';
import {Component, ViewEncapsulation} from '@angular/core';
import {CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { jamGithub } from '@ng-icons/jam-icons';
import {LogoComponent} from "./logo/logo.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NgIcon, LogoComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  encapsulation: ViewEncapsulation.None,
  viewProviders: [provideIcons({ jamGithub })]
})
export class AppComponent {
}
