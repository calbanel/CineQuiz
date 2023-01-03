import { AnsweredQuestion } from "./answeredquestion.models";

export class Game{
    date !: string;
    score !: number;
    questions !: AnsweredQuestion[];
}