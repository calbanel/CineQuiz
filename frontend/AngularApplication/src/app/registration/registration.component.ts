import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { User } from '../models/user.models';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Component({
  selector: 'app-inscription',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm !: FormGroup;

  constructor(private formBuilder: FormBuilder, private userService: UserService, 
    private router: Router,private http: HttpClient) { }

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      pseudo: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
      confirmPassword: [null, [Validators.required]],
    },
    { 
      validator: this.matchPassword() 
    });
  }

  matchPassword() : ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const pwd = control.get('password')!.value;
      const confirm = control.get('confirmPassword')!.value;
 
      if (pwd != confirm) { 
        return { 'notMatching': true };
      }
      return null
    }
  }

  isValid(field : string) {
    return this.registrationForm.get(field)?.valid;
  }

  getField(field : string) {
    return this.registrationForm.get(field);
  }
 
  onSubmitForm(): void {
    console.log(this.registrationForm.value);
    this.http.post<User>("http://localhost:8080/add-user",this.registrationForm.value)
    .subscribe(result => {
      console.log(result);
      setTimeout(() => {this.router.navigateByUrl("/");},1000);
    });

  }

}
