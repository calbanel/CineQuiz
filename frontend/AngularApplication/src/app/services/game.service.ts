import { DatePipe } from "@angular/common";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable, throwError } from "rxjs";
import { environment } from "src/environments/environment";
import Swal from "sweetalert2";
import { AnsweredQuestion } from "../models/answeredquestion.models";
import { Game } from "../models/game.models";
import { Question } from "../models/question.model";
import { QuestionList } from "../models/questionlist.models";
import { AccountService } from "./account.service";

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
  currentGame !: Game;

  constructor(private http: HttpClient, private account : AccountService, private router : Router, private datepipe: DatePipe) {

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

          let date : string = "NaN";
          let supposedDate : string | null = this.datepipe.transform(Date.now(), 'dd/MM/yyyy HH:mm:ss');
          if(supposedDate != null)
            date = supposedDate;
        
          this.currentGame = new Game(date,this.score);
          
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

  addAnsweredQuestionToCurrentGame(num : number, answer : string, answerTime : number, question : Question, score : number){
    if(num <= environment.nbQuestionsInQuiz && num > 0){
      let answeredQuestion : AnsweredQuestion = new AnsweredQuestion(score,question,answer,answerTime);
      this.currentGame.questions[num-1] = answeredQuestion;
    }
  }

  gameEnd(){
    this.currentGame.score = this.score;
    this.http.post<Game>(`${environment.apiUrl}/add-game-to-user/${this.account.userValue.id}`, this.currentGame)
            .subscribe({ next: result => {
                Swal.fire('La partie est terminé!','Tu peux retrouver tes réponses dans ton historique de parties','success')
                setTimeout(() => { this.router.navigateByUrl("/"); }, 2000);
            },
            error: (err) => Swal.fire('La partie est terminé!','Echec de sauvegarde de la partie','error')
        });
  }
}