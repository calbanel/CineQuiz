import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';
import { GameService } from '../services/game.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnChanges{

  user !: User;

  constructor(private account: AccountService, private game: GameService, private router: Router) {
      this.user = this.account.userValue;
  }

  ngOnChanges(changes: SimpleChanges): void {
      this.user = this.account.userValue;
  }

  isLoggedIn() : boolean{
    let isLogged : boolean = this.account.isLoggedIn;
    if(isLogged)
      this.user = this.account.userValue;
    return isLogged;
  }

  logout(){
    this.account.logout();
  }

  signUpPage(){
    this.router.navigateByUrl("/login");
  }

  signInPage(){
    this.router.navigateByUrl("/sign-in");
  }

  home(){
    this.router.navigateByUrl("/");
  }

}
