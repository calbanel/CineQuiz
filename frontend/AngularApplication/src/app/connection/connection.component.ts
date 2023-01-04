import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccountService } from '../services/account.service';
import * as CryptoJS from 'crypto-js';
import { Observable } from 'rxjs/internal/Observable';
import { map } from 'rxjs';

@Component({
  selector: 'app-connection',
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.css']
})
export class ConnectionComponent implements OnInit {

  connectionForm !: FormGroup;
  submitted = false;
  showEmailErrors$ !: Observable<Boolean>;

  constructor(private formBuilder: FormBuilder,
    private accountService: AccountService) { }

  ngOnInit(): void {
    this.connectionForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    });

    this.showEmailErrors$ = this.connectionForm.statusChanges.pipe(
      map(status => status === 'INVALID' &&
        this.connectionForm.get('email')!.dirty &&
        this.connectionForm.get('email')!.invalid));
  }

  onSubmitForm() {
    let encryptedPassword = CryptoJS.SHA3(this.connectionForm.value.password, { outputLength: 224 }).toString();
    let credentials = { email: this.connectionForm.value.email, password: encryptedPassword };
    this.accountService.login(credentials);
  }

}
