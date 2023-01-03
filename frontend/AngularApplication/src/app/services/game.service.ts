import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable, ɵisListLikeIterable } from "@angular/core";
import { Question } from "../models/question.model";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { QuestionList } from "../models/questionlist.models";

@Injectable({
  providedIn: 'root'
})
export class GameService {
  gameLaunched : boolean = false;
  questionList !: QuestionList;
  score : number = 0;

  constructor(private http: HttpClient) {

  }

  generateQuiz(nb:number): Observable<boolean> {

    const creation = new Observable<boolean>(observer => {
      if(!this.gameLaunched){
        let queryParams = new HttpParams().append("number",nb);
        this.http.get<QuestionList>(`${environment.apiUrl}/questions/random-list`,{params:queryParams}).subscribe(val => {
          this.questionList = val;
          observer.next(this.isQuizOK());
          observer.complete();
        })
      } else {
        observer.next(false);
        observer.complete();
      }
    })
    return creation;
  }

  isQuizOK() : boolean {
    let ok : boolean = false;
    if(this.questionList != undefined && this.questionList != null){
      this.gameLaunched = true;
      ok = true;
    }
    return ok;
  }
}