import { Component, OnInit } from '@angular/core';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})
export class RankingComponent implements OnInit {
  user !: User;

  constructor(public account : AccountService) {
    this.user = this.account.userValue;
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
