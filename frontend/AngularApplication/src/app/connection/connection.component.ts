import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-connection',
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.css']
})
export class ConnectionComponent implements OnInit {

  connectionForm !: FormGroup;

  constructor(private formBuilder:FormBuilder) { }

  ngOnInit(): void {
    this.connectionForm = this.formBuilder.group({
      pseudo: [null, Validators.required],
      password: [null, Validators.required]
    });
  }

  onSubmitForm() : void {
    console.log(this.connectionForm.value);
  }

}
