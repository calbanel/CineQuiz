import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'CineQuiz';
}

@Component({
  selector: 'question',
  templateUrl: './question/question.component.html',
  styleUrls: ['./question/question.component.css']
})
export class QuestionComponent {
  title = 'QuestionPage';
}
