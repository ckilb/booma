import {Component, ViewEncapsulation} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, FormsModule, NgModel, ReactiveFormsModule, Validators} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {InputTextComponent} from "../input-text/input-text.component";
import {ButtonPrimaryComponent} from "../button-primary/button-primary.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, InputTextComponent, ButtonPrimaryComponent],
  templateUrl: './login.component.html',
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent {
  form: FormGroup

  constructor(
    private router: Router,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      passphrase: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(50)]],
    });

    this.form.valueChanges.subscribe((data) => {
      const passphrase: string = data['passphrase'];

      const slugged = passphrase
        .toLowerCase()
        .replaceAll(' ', '-')
        .replaceAll('_', '-')
        .replaceAll(/[^a-z0-9\-]/g, '')
        .replaceAll(/-+/g, '-');

      if (slugged === passphrase) {
        return;
      }

      this.form.get('passphrase')?.setValue(slugged);
    })
  }

  login() {
    const passphrase = this.form.get('passphrase')?.value || '';
    const slug = passphrase.replaceAll(' ', '-').toLocaleLowerCase();

    void this.router.navigate(['/bookmarks', slug]);
  }
}
