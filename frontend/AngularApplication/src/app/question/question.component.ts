import { Component, Input } from '@angular/core';
import { Question } from '../models/question.model';

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css']
})
export class QuestionComponent {
  @Input() quest!: Question;

  onClick(answerClicked:string){
    console.log(answerClicked);
    if(answerClicked==this.quest.answer){
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#78e08f");
    }else{
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#E55039");
      document.getElementById(this.quest.answer)?.setAttribute("style", "background-color:#78e08f");
    }
  }
}
