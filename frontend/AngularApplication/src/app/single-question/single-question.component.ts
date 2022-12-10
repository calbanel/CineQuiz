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
  // quest$!: Observable<Question>;
  quest !: Question;
  answered: boolean = false;
  someSubscription: any;

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
    let currentId = +this.route.snapshot.params['id'];
    this.quest = this.questionService.getQuestionByNumber(currentId);
    
    //this.quest.questionNumber = currentId;
    // this.quest$ = this.questionService.getQuestionByNumber();
  }

  onClick(answerClicked: string) {
    this.answered = true;
    console.log(answerClicked);
    if (answerClicked == this.quest.answer) {
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#78e08f");
    } else {
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#E55039");
      document.getElementById(this.quest.answer)?.setAttribute("style", "background-color:#78e08f");
    }
  }

  onNextQuestion() {
    this.router.navigateByUrl(`/questions/${this.quest.questionNumber + 1}`);
  }

  ngOnDestroy() {
    if (this.someSubscription) {
      this.someSubscription.unsubscribe();
    }
  }

}
