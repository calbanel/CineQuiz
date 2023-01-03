import { Question } from "./question.model";

export class AnsweredQuestion{
    score !: number;
    question !: Question;
    answer !: string;
    answerTime !: number;
}