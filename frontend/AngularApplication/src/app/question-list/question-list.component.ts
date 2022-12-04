import { Component, OnInit } from '@angular/core';
import { Question } from '../models/question.model';
import { QuestionService } from '../services/questions.service';

@Component({
  selector: 'app-question-list',
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.css']
})
export class QuestionListComponent implements OnInit {
  questions! :Question[];

  constructor(private questionService: QuestionService) { }

  ngOnInit(): void {
    this.questions = this.questionService.getAllQuestions();
  }

}
