export class Question{

    constructor(public questionNumber:number, 
                public type:string,
                public question: string,
                public description:string,
                public answerA:string,
                public answerB:string,
                public answerC:string,
                public answerD:string,
                public answer:string,
                public media:string){
    }
}