import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
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
    return this.http.get<Question>("http://localhost:8080/api/questions/random");
  }

  getQuestionList(nb:number): Observable<Question[]> {
    let queryParams = new HttpParams().append("number",nb);
    return this.http.get<Question[]>("http://localhost:8080/api/questions/random-list",{params:queryParams});
  }

}