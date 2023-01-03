import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable, ɵisListLikeIterable } from "@angular/core";
import { Question } from "../models/question.model";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { QuestionList } from "../models/questionlist.models";

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  questionList !: QuestionList;

  constructor(private http: HttpClient) {

  }

  getQuestion(): Observable<Question> {
    return this.http.get<Question>(`${environment.apiUrl}/questions/random`);
  }

  getQuestionList(nb:number): Observable<QuestionList> {
    let queryParams = new HttpParams().append("number",nb);
    return this.http.get<QuestionList>(`${environment.apiUrl}/questions/random-list`,{params:queryParams})
  }
}