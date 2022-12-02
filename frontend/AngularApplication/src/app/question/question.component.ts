import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css']
})
export class QuestionComponent implements OnInit {
  title!:string;
  questionNumber!:number;
  type!:string;
  question!: string;
  description!: string;
  answerA!: string;
  answerB!: string;
  answerC!: string;
  answerD!: string;
  answer!: string;
  media!: string;

  ngOnInit(){
    this.title='Question';
    this.questionNumber = 1;
    this.type = "wich-by-image";
    this.question = "Quel est ce film ?";
    this.description = "";
    this.answerA = "Un homme d’exception";
    this.answerB = "Invincible : Le chemin de la rédemption";
    this.answerC = "21 grammes";
    this.answerD = "Sissi Impératrice";
    this.answer = "answerB";
    this.media= "https://image.tmdb.org/t/p/w500/6nh02WC31a5Sg3HqK21JY1nHqFo.jpg";
  }

  onClick(answerClicked:string){
    console.log(answerClicked);
    if(answerClicked==this.answer){
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#78e08f");
    }else{
      document.getElementById(answerClicked)?.setAttribute("style", "background-color:#E55039");
      document.getElementById(this.answer)?.setAttribute("style", "background-color:#78e08f");
    }
  }
}
