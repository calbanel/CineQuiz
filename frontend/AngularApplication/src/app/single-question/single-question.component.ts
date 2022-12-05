import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Question } from '../models/question.model';
import { QuestionService } from '../services/questions.service';

@Component({
  selector: 'app-single-question',
  templateUrl: './single-question.component.html',
  styleUrls: ['./single-question.component.css']
})
export class SingleQuestionComponent implements OnInit {

  currentId !:number;
  quest!: Question;
  answered:boolean = false;

  constructor(private questionService: QuestionService,
              private route: ActivatedRoute,
              private router: Router) {}

  ngOnInit(): void {
    this.currentId = +this.route.snapshot.params['id'];
    this.quest = this.questionService.getQuestionByNumber(this.currentId);
  }

  onClick(answerClicked:string){
    this.answered=true;
    console.log(answerClicked);
    if(answerClicked==this.quest.answer){
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#78e08f");
    }else{
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#E55039");
      document.getElementById(this.quest.answer+this.quest.questionNumber)?.setAttribute("style", "background-color:#78e08f");
    }
  }

  onNextQuestion(){
    this.majQuestion();
    this.router.navigateByUrl(`/questions/${this.quest.questionNumber+1}`);
  }

  majQuestion(){
    this.currentId = +this.route.snapshot.params['id'];
    if(this.currentId === 1){
      this.currentId = 2;
    }
    this.quest = this.questionService.getQuestionByNumber(this.currentId);
  }

}
