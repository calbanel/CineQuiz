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
    const options = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': 'Content-Type, Authorization'
      })
    }
    return this.http.get<Question>("http://localhost:8080/cinequiz/questions/movie/random", options);
  }

}