import { Component, OnInit } from '@angular/core';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent implements OnInit {
  user !: User;

  constructor(public account: AccountService) {
    this.user = this.account.userValue;
    this.user.score = 0;
  }

  isLoggedIn() : boolean{
    return this.account.isLoggedIn;
  }

  logout(){
    this.account.logout();
  }
  
  ngOnInit(): void {
  }

}
