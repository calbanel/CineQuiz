import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Question } from '../models/question.model';
import { QuestionList } from '../models/questionlist.models';
import { environment } from 'src/environments/environment';
import { GameService } from '../services/game.service';

const TIME_TO_ANSWER = 15;
const TIME_BETWEEN_TWO_QUESTIONS = 3;
const MILISECONDS_FOR_ONE_SECOND = 1000;

const TIMING_TO_GET_MAX_SCORE_ON_ONE_QUESTION = 3;
const MAX_SCORE_FOR_ONE_QUESTION = 1000;
@Component({
  selector: 'app-single-question',
  templateUrl: './single-question.component.html',
  styleUrls: ['./single-question.component.css']
})
export class SingleQuestionComponent implements OnInit, OnDestroy {
  questionList !: QuestionList;
  currentQuestion !: Question;
  answered: boolean = false;
  someSubscription: any;
  questionNumber !: number;
  marky !: any;
  answerTimeInSeconds !: number;
  timeToAnswer: number = TIME_TO_ANSWER;
  interval !: any;
  score !: number;
  questionGameID !: string;
  currentAnswer !: string;

  constructor(private game: GameService, private route: ActivatedRoute,
    private router: Router) {

    this.questionNumber = +this.route.snapshot.params['id'];
    if(this.questionNumber != game.currentQuestion || !game.gameLaunched){
      this.router.navigateByUrl("/");
      this.ngOnDestroy();
    }

    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };
    this.someSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.router.navigated = false;
      }
    });

    this.questionGameID = game.gameID;
    this.questionList = game.questionList;
    this.score = this.game.score;

    this.currentAnswer = "";
    this.answered = false;
    this.currentQuestion = this.questionList.list[this.questionNumber-1];
  }

  ngOnInit(): void {
    this.marky = require('marky');
    this.marky.mark('answerTime');
    this.startQuestionTimer();
  }

  startQuestionTimer() {
    this.interval = setInterval(() => {
      this.timeToAnswer--;
      if(this.timeToAnswer <= 0) {
        clearInterval(this.interval);
        this.onClick("", this.currentQuestion);
      }
    },MILISECONDS_FOR_ONE_SECOND)
  }

  onClick(answerClicked: string, question: Question) {
    if(!this.answered){
      this.answered = true;
      this.answerTimeInSeconds = this.marky.stop('answerTime')['duration'] / MILISECONDS_FOR_ONE_SECOND;
      this.currentAnswer = answerClicked;

      if (answerClicked === question.answer) {
        this.answered = true;
        document.getElementById(answerClicked)?.setAttribute("style", "background-color:#78e08f");
        if (this.answerTimeInSeconds <= TIMING_TO_GET_MAX_SCORE_ON_ONE_QUESTION) {
          this.score += MAX_SCORE_FOR_ONE_QUESTION;
        } else {
          this.score += Math.round((1 - (this.answerTimeInSeconds / TIME_TO_ANSWER) / 2) * MAX_SCORE_FOR_ONE_QUESTION);
        }
      } else {
        document.getElementById(answerClicked)?.setAttribute("style", "background-color:#E55039");
        document.getElementById(question.answer)!.setAttribute("style", "background-color:#78e08f");
      }

      clearInterval(this.interval);
      setTimeout(() => { this.nextQuestion() }, TIME_BETWEEN_TWO_QUESTIONS*1000);
    }
  }

  nextQuestion() {
    if(this.game.gameID == this.questionGameID){
      this.game.addAnsweredQuestionToCurrentGame(this.questionNumber,this.currentAnswer,this.answerTimeInSeconds,this.currentQuestion, this.score - this.game.score);
      this.game.score = this.score;
      if (this.questionNumber == environment.nbQuestionsInQuiz) {
        this.game.gameEnd();
      } else {
        this.game.currentQuestion += 1;
        this.router.navigateByUrl(`/questions/${this.game.currentQuestion}`);
      }
    }
  }

  ngOnDestroy() {
    if (this.someSubscription) {
      this.someSubscription.unsubscribe();
    }
  }

}
