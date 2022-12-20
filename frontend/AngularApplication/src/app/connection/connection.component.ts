import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../services/account.service';
import { first } from 'rxjs/operators';

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
              private accountService: AccountService,) { }

  ngOnInit(): void {
    this.connectionForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    });
  }

  onSubmitForm(): void {
    console.log(this.connectionForm.value);
    this.submitted = true;
    if (this.connectionForm.invalid) {
      return;
    }
    this.loading = true;
    let encryptedPassword = CryptoJS.SHA3(this.connectionForm.value.password, { outputLength: 224 }).toString();
    this.accountService.login(this.connectionForm.value.email,encryptedPassword)
      .pipe(first())
      .subscribe({
        next: () => {
          this.router.navigateByUrl('/login');
        },
        error: error => {
          this.loading = false;
        }
      });
  }

}
