import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable, ɵisListLikeIterable } from "@angular/core";
import { Question } from "../models/question.model";
import { Observable, throwError } from "rxjs";
import { environment } from "src/environments/environment";
import { QuestionList } from "../models/questionlist.models";

const BASE_GAME_ID : string = "-1"
@Injectable({
  providedIn: 'root'
})
export class GameService {
  gameID : string = BASE_GAME_ID;
  gameLaunched : boolean = false;
  questionList !: QuestionList;
  currentQuestion : number = 1;
  score : number = 0;

  constructor(private http: HttpClient) {

  }

  genUniqueGameId(): string {
    const dateStr = Date
      .now()
      .toString(36);
  
    const randomStr = Math
      .random()
      .toString(36)
      .substring(2, 8);
  
    return `${dateStr}-${randomStr}`;
  }

  generateQuiz(nb:number): Observable<boolean> {

    const creation = new Observable<boolean>(observer => {
        let queryParams = new HttpParams().append("number",nb);
        this.http.get<QuestionList>(`${environment.apiUrl}/questions/random-list`,{params:queryParams}).subscribe(
          { next: val => {
          this.questionList = val;
          this.gameID = this.genUniqueGameId();
          observer.next(this.isQuizOK());
          observer.complete();
          },
          error: (err) => throwError(() => new Error(err))
        });
    })
    return creation;
  }

  isQuizOK() : boolean {
    let ok : boolean = false;
    if(this.questionList != undefined && this.questionList != null && this.questionList.list.length == environment.nbQuestionsInQuiz && this.gameID != BASE_GAME_ID){
      this.gameLaunched = true;
      ok = true;
    }
    return ok;
  }

  reset() {
    this.gameID = BASE_GAME_ID;
    this.gameLaunched = false;
    this.currentQuestion = 1;
    this.score = 0;
  }
}