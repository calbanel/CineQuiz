import { Question } from "./question.model";

export class AnsweredQuestion{
    score !: number;
    question !: Question;
    answer !: string;
    answerTime !: number;

    constructor(score: number, question : Question, answer : string, answerTime : number){
        this.score = score;
        this.question = question;
        this.answer = answer;
        this.answerTime = answerTime;
    }
}