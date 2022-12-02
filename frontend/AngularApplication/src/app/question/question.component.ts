import { Component } from '@angular/core';

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css']
})
export class QuestionComponent {
  title='Question';
  questionNumber:number = 1;
  type:string = "wich-by-image";
  question: string|null = "Quel est ce film ?";
  description: string|null = "Une charmante petite ville de l'Angleterre rurale. Si elle est originale et artiste dans l'âme, Louisa «Lou» Clark, 26 ans, n'a aucune ambition particulière. Elle se contente d'enchaîner les boulots pour permettre à ses proches de joindre les deux bouts. Jeune et riche banquier, Will Traynor était un garçon plein d'audace et d'optimisme jusqu'à ce qu'il se retrouve paralysé, suite à un accident survenu deux ans plus tôt. Devenu cynique, il a renoncé à tout et n'est plus que l'ombre de lui-même. Autant dire que ces deux-là auraient pu ne jamais se rencontrer. Mais lorsque Lou accepte de travailler comme aide-soignante auprès de Will, elle est bien décidée à lui redonner goût à la vie. Et peu à peu, les deux jeunes gens s'éprennent passionnément l'un de l'autre. La force de leur amour pourra-t-elle survivre à leur destin qui semble inexorable?";
  answerA: string|null = "Un homme d’exception";
  answerB: string|null = "Invincible : Le chemin de la rédemption";
  answerC: string|null = "21 grammes";
  answerD: string|null = "Sissi Impératrice";
  answer: string|null = "Invincible : Le chemin de la rédemption";
  media: string|null = "https://image.tmdb.org/t/p/w500/6nh02WC31a5Sg3HqK21JY1nHqFo.jpg";

  onClick(answerClicked:any){
    console.log(answerClicked);
    if(answerClicked==this.answer){
      alert("Bonne reponse");
    }else{
      alert("Mauvaise reponse");
    }

    
  }
}
