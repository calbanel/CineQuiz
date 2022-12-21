import { Component, OnInit } from '@angular/core';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';
import { AppService } from '../services/app.service';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  user !: User;

  constructor(private accountService: AccountService,private app: AppService) {
      this.user = this.accountService.userValue;
  }

  authenticated() { return this.app.authenticated; }

  ngOnInit() {
    
  }
}
