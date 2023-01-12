import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from "@angular/common/http";
import * as CryptoJS from 'crypto-js';
import { matchPasswordsValidator } from '../validators/match-passwords';
import { map, Observable } from 'rxjs';
import { AccountService } from '../services/account.service';
import { patternValidator } from '../validators/regex';

@Component({
  selector: 'app-inscription',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm !: FormGroup;
  showPasswordErrors$ !: Observable<Boolean>;
  showEmailErrors$ !: Observable<Boolean>;

  showPasswordLengthError$ !: Observable<Boolean>;
  showHasNumberError$ !: Observable<Boolean>;
  showHasUpperCaseError$ !: Observable<Boolean>;

  constructor(private formBuilder: FormBuilder, private http: HttpClient, private accountService: AccountService) { }

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      pseudo: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, Validators.compose([
        Validators.required,
        patternValidator(/\d/, { hasNumberError: true }),
        patternValidator(/[A-Z]/, { hasUpperCaseError: true }),
        Validators.minLength(8)])
      ],
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

    this.showPasswordLengthError$ = this.registrationForm.statusChanges.pipe(
      map(status => status === 'INVALID' &&
        this.registrationForm.get('password')!.dirty &&
        this.registrationForm.get('password')!.hasError('minlength')));

    this.showHasNumberError$ = this.registrationForm.statusChanges.pipe(
      map(status => status === 'INVALID' &&
        this.registrationForm.get('password')!.dirty &&
        this.registrationForm.get('password')!.hasError('hasNumberError')));

    this.showHasUpperCaseError$ = this.registrationForm.statusChanges.pipe(
      map(status => status === 'INVALID' &&
        this.registrationForm.get('password')!.dirty &&
        this.registrationForm.get('password')!.hasError('hasUpperCaseError')));
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
