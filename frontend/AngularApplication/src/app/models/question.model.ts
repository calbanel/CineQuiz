import { Choice } from "./choice.models";

export class Question{
    optionalImage!:string;
    optionalText!:string;
    choices !: Choice;
    question!: string;
    answer!:string;
}