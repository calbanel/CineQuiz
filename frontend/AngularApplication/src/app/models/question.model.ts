import { Choice } from "./choice.models";

export class Question{
    optionalImage!:string;
    optionalText!:string;
    question!: string;
    choices !: Choice;
    answer!:string;
}