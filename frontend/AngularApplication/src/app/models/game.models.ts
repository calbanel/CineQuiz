import { environment } from "src/environments/environment";
import { AnsweredQuestion } from "./answeredquestion.models";

export class Game{
    date !: string;
    score !: number;
    questions !: AnsweredQuestion[];

    constructor(date : string, score : number){
        this.date = date;
        this.score = score;
        this.questions = new Array<AnsweredQuestion>(environment.nbQuestionsInQuiz);
    }
}