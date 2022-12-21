import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  user !: User;

  constructor(private accountService: AccountService, private router : Router) {
      this.user = this.accountService.userValue;
  }

  getUser(){
    return this.user;
  }

  logout(){
    this.accountService.logout();
  }

  ngOnInit() {
    
  }
}
