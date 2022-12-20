import { Component, OnInit } from '@angular/core';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  user: User;

  constructor(private accountService: AccountService) {
      this.user = this.accountService.userValue;
  }
  ngOnInit() {
    
  }
}
