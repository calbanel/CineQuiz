import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from '../models/user.models';
import { UserService } from '../services/user.service';
import { Observable } from "rxjs";

@Component({
  selector: 'app-inscription',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm !: FormGroup;

  constructor(private formBuilder:FormBuilder, private userService:UserService) { }

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      pseudo: [null, [Validators.required]],
      email: [null,  [Validators.required, Validators.email]],
      password: [null,  [Validators.required]],
      confirmPassword: [null,  [Validators.required]]
    });
  }

  onSubmitForm() : void {
    console.log(this.registrationForm.value);
    let user:User = { id: 1,
                      pseudo: this.registrationForm.value.pseudo,
                      email : this.registrationForm.value.email,
                      password: this.registrationForm.value.password
    }
    console.log(user);
    this.userService.addUser(user).subscribe();
  }

}
