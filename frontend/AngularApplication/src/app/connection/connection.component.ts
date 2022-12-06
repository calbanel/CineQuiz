import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

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
      pseudo: [null],
      password: [null]
    });
  }

  onSubmitForm() : void {
    console.log(this.connectionForm.value);
  }

}
