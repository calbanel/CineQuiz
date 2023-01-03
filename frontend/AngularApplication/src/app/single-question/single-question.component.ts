import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Question } from '../models/question.model';
import { QuestionService } from '../services/questions.service';
import { interval, Observable, timer } from 'rxjs';
import { User } from '../models/user.models';
import { AccountService } from '../services/account.service';

const TIME_TO_ANSWER = 15;
const TIME_BETWEEN_TWO_QUESTIONS = 3;
const MILISECONDS_FOR_ONE_SECOND = 1000;

const TIMING_TO_GET_MAX_SCORE_ON_ONE_QUESTION = 4;
const MAX_SCORE_FOR_ONE_QUESTION = 1000;

const NB_QUESTIONS_IN_QUIZ = 10;
@Component({
  selector: 'app-single-question',
  templateUrl: './single-question.component.html',
  styleUrls: ['./single-question.component.css']
})
export class SingleQuestionComponent implements OnInit, OnDestroy {
  quest$!: Observable<Question>;
  answered: boolean = false;
  someSubscription: any;
  questionNumber !: number;
  marky !: any;
  answerTimeInSeconds !: number;
  user !: User;
  timeToAnswer: number = TIME_TO_ANSWER;
  interval !: any;

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
    this.startQuestionTimer();
  }

  ngOnInit(): void {
    this.answered = false;
    this.questionNumber = +this.route.snapshot.params['id'];
    this.quest$ = this.questionService.getQuestion();
    this.marky = require('marky');
    this.marky.mark('answerTime');

  }

  startQuestionTimer() {
    this.interval = setInterval(() => {
      if(this.timeToAnswer > 0) {
        this.timeToAnswer--;
      } else {
        this.timeToAnswer = TIME_TO_ANSWER;
        clearInterval(this.interval);
        this.nextQuestion();
      }
    },MILISECONDS_FOR_ONE_SECOND)
  }

  onClick(answerClicked: string, question: Question) {
    this.answerTimeInSeconds = this.marky.stop('answerTime')['duration'] / MILISECONDS_FOR_ONE_SECOND;
    
    if (answerClicked === question.answer && this.answered === false) {
      this.answered = true;
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#78e08f");
      document.getElementById(answerClicked)?.setAttribute('disabled', '');
      if (this.answerTimeInSeconds <= TIMING_TO_GET_MAX_SCORE_ON_ONE_QUESTION) {
        this.user.score += MAX_SCORE_FOR_ONE_QUESTION;
      } else {
        this.user.score += Math.round((1 - (this.answerTimeInSeconds / TIME_TO_ANSWER) / 2) * MAX_SCORE_FOR_ONE_QUESTION);
      }
    } else {
      this.answered = true;
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#E55039");
      document.getElementById(question.answer)!.setAttribute("style", "background-color:#78e08f");
    }
  }

  nextQuestion() {
    if (this.questionNumber == NB_QUESTIONS_IN_QUIZ) {
      this.router.navigateByUrl("/ranking");
    } else {
      this.router.navigateByUrl(`/questions/${this.questionNumber + 1}`);
    }
  }

  isLoggedIn(): boolean {
    return this.account.isLoggedIn;
  }

  logout() {
    this.account.logout();
  }

  ngOnDestroy() {
    if (this.someSubscription) {
      this.someSubscription.unsubscribe();
    }
  }

}
