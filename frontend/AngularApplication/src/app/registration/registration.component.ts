import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from "@angular/common/http";
import * as CryptoJS from 'crypto-js';
import { matchPasswordsValidator } from '../validators/match-passwords';
import { map, Observable } from 'rxjs';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-inscription',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm !: FormGroup;
  showPasswordErrors$ !: Observable<Boolean>;
  showEmailErrors$ !: Observable<Boolean>;

  constructor(private formBuilder: FormBuilder, private http: HttpClient, private accountService: AccountService) { }

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      pseudo: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
      confirmPassword: [null, [Validators.required]],
    },
      {
        validator: [matchPasswordsValidator('password', 'confirmPassword')]
      });

    this.showPasswordErrors$ = this.registrationForm.statusChanges.pipe(
      map(status => status === 'INVALID' &&
        this.registrationForm.get('password')?.value &&
        this.registrationForm.get('confirmPassword')?.dirty &&
        this.registrationForm.hasError('confirmEqual')));

    this.showEmailErrors$ = this.registrationForm.statusChanges.pipe(
      map(status => status === 'INVALID' &&
        this.registrationForm.get('email')!.dirty &&
        this.registrationForm.get('email')!.invalid));
  }

  onSubmitForm(): void {
    let encryptedPassword = CryptoJS.SHA3(this.registrationForm.value.password, { outputLength: 224 }).toString();
    let newUser = {
      pseudo: this.registrationForm.value.pseudo,
      email: this.registrationForm.value.email,
      password: encryptedPassword,
      games: new Array
    }
    this.accountService.register(newUser);
  }

}
