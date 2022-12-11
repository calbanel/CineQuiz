import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Question } from '../models/question.model';
import { QuestionService } from '../services/questions.service';
import { Observable } from 'rxjs';

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

  constructor(private questionService: QuestionService, private route: ActivatedRoute,
    private router: Router) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };
    this.someSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.router.navigated = false;
      }
    });
  }

  ngOnInit(): void {
    this.answered = false;
    this.questionNumber = +this.route.snapshot.params['id'];
    this.quest$ = this.questionService.getQuestion();
  }

  onClick(answerClicked: string, answer: string) {
    this.answered = true;
    console.log(answerClicked);
    console.log(answer);
    if (answerClicked === answer) {
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#78e08f");
    } else {
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#E55039");
      document.getElementById(answer)!.setAttribute("style", "background-color:#78e08f");
    }
  }

  onNextQuestion() {
    this.router.navigateByUrl(`/questions/${this.questionNumber + 1}`);
  }

  ngOnDestroy() {
    if (this.someSubscription) {
      this.someSubscription.unsubscribe();
    }
  }

}
