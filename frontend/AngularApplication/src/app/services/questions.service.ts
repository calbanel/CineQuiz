import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Question } from "../models/question.model";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  constructor(private http: HttpClient) {

  }

  getQuestion(): Observable<Question> {
    return this.http.get<Question>("http://localhost:8080/cinequiz/questions/movie/random");
  }

}