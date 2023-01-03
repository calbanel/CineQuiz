import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { Question } from '../models/question.model';
import { QuestionList } from '../models/questionlist.models';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';
import { QuestionService } from '../services/questions.service';


@Component({
  selector: 'app-loading-game',
  templateUrl: './loading-game.component.html',
  styleUrls: ['./loading-game.component.css']
})
export class LoadingGameComponent implements OnInit, OnDestroy {
  someSubscription: any;
  user !: User;
  questions !: Question[];

  constructor(private questionService: QuestionService, private route: ActivatedRoute,
    private router: Router, public account: AccountService) {
      this.router.routeReuseStrategy.shouldReuseRoute = function () {
        return false;
      };
      this.someSubscription = this.router.events.subscribe((event) => {
        if (event instanceof NavigationEnd) {
          this.router.navigated = false;
        }
      });
      this.user = this.account.userValue;

      questionService.getQuestionList(environment.nbQuestionsInQuiz).subscribe(val => {
        questionService.questionList = val;
        this.router.navigateByUrl(`/questions/1`);
      })
  }

  ngOnDestroy() {
  
  }

  isLoggedIn() : boolean{
    return this.account.isLoggedIn;
  }

  logout(){
    this.account.logout();
  }

  ngOnInit() {
    
  }
}
