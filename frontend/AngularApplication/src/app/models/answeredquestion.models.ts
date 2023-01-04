import { Question } from "./question.model";

export class AnsweredQuestion{
    score !: number;
    question !: Question;
    playerAnswer !: string;
    answerTime !: number;

    constructor(score: number, question : Question, playerAnswer : string, answerTime : number){
        this.score = score;
        this.question = question;
        this.playerAnswer = playerAnswer;
        this.answerTime = answerTime;
    }
}