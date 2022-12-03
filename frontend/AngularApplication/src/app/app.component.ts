import { Component, OnInit } from '@angular/core';
import { Question } from './models/question.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  myQuest! :Question;

  ngOnInit(){
    console.log("init root");
    this.myQuest = new Question(1,'wich-by-image','Quel est ce film ?','',
    'Un homme d’exception','Invincible : Le chemin de la rédemption','21 grammes',
    'Sissi Impératrice','answerB','https://image.tmdb.org/t/p/w500/6nh02WC31a5Sg3HqK21JY1nHqFo.jpg');
    console.log(this.myQuest);
  }
}