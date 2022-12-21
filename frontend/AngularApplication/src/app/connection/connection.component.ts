import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../services/account.service';
import { first } from 'rxjs/operators';
import * as CryptoJS from 'crypto-js';
import { AppService } from '../services/app.service';

@Component({
  selector: 'app-connection',
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.css']
})
export class ConnectionComponent implements OnInit {

  connectionForm !: FormGroup;
  loading = false;
  submitted = false;

  constructor(private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private app: AppService) { }

  ngOnInit(): void {
    this.connectionForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    });
  }

  onSubmitForm() {
    console.log(this.connectionForm.value);
    this.loading = true;
    let encryptedPassword = CryptoJS.SHA3(this.connectionForm.value.password, { outputLength: 224 }).toString();
    let credentials = { email: this.connectionForm.value.email, password: encryptedPassword };
    this.app.authenticate(credentials, () => {
      this.router.navigateByUrl('/');
    });
    return false;
  }

}
