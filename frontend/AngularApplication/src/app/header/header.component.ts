import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnChanges{

  user !: User;

  constructor(public account: AccountService) {
      this.user = this.account.userValue;
  }

  ngOnChanges(changes: SimpleChanges): void {
      this.user = this.account.userValue;
  }

  isLoggedIn() : boolean{
    return this.account.isLoggedIn;
  }

  logout(){
    this.account.logout();
  }

}
