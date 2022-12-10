import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Question } from "../models/question.model";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  constructor(private http: HttpClient) {

  }

  questions: Question[] = [
    // {
    //   question: 'Quel est ce film ?',
    //   optionalText: '',
    //   choice1: 'Un homme d’exception',
    //   choice2: 'Invincible : Le chemin de la rédemption',
    //   choice3: '21 grammes',
    //   choice4: 'Sissi Impératrice',
    //   answer: 'Invincible : Le chemin de la rédemption',
    //   optionalImage: 'https://image.tmdb.org/t/p/w500/6nh02WC31a5Sg3HqK21JY1nHqFo.jpg'
    // },
    // {
    //   question: 'Quel a été le budget de ce film ?',
    //   optionalText: 'Solo: A Star Wars Story',
    //   choice1: '817400891',
    //   choice2: '1850000',
    //   choice3: '121214377',
    //   choice4: '392952373',
    //   answer: '392952373',
    //   optionalImage: 'https://image.tmdb.org/t/p/w500/ojHCeDULAkQK25700fhRU75Tur2.jpg'
    // },
    // {
    //   question: 'Quelle personne n\'a pas participé à ce film ?',
    //   optionalText: 'Star Wars : Les Derniers Jedi',
    //   choice1: 'Kate Dickie',
    //   choice2: 'Whoopi Goldberg',
    //   choice3: 'Gwendoline Christie',
    //   choice4: 'Daisy Ridley',
    //   answer: 'Whoopi Goldberg',
    //   optionalImage: 'https://image.tmdb.org/t/p/w500/5Iw7zQTHVRBOYpA0V6z0yypOPZh.jpg'
    // },
    // {
    //   question: 'A quelle date est sortie ce film ?',
    //   optionalText: 'Looop Lapeta : La boucle infernale',
    //   choice1: '2006-05-10',
    //   choice2: '2022-02-04',
    //   choice3: '1999-04-16',
    //   choice4: '2002-10-18',
    //   answer: '2022-02-04',
    //   optionalImage: 'https://image.tmdb.org/t/p/w500/kQM7o3NIkruIZLoQ9E2XzZQ8Ujl.jpg'
    // },
    // {
    //   question: 'Quel a été le revenu généré par ce film ?',
    //   optionalText: 'G.I. Joe : Le Réveil du Cobra',
    //   choice1: '77000000',
    //   choice2: '16951798',
    //   choice3: '302469017',
    //   choice4: '26000000',
    //   answer: '302469017',
    //   optionalImage: 'https://image.tmdb.org/t/p/w500/vBV2nF3yqYrskq2y1bSSuAuGqcz.jpg'
    // },
    // {
    //   question: 'Quelle personne a participé à ce film ?',
    //   optionalText: 'Superman',
    //   choice1: 'Eric Lloyd',
    //   choice2: 'Terence Stamp',
    //   choice3: 'Alan Arkin',
    //   choice4: 'Tim Allen',
    //   answer: 'Terence Stamp',
    //   optionalImage: 'https://image.tmdb.org/t/p/w500/v6MVBFnQOscITvmAy5N5ras2JKZ.jpg'
    // },
    // {
    //   question: 'A quel film correspond a cette optionalText ?',
    //   optionalText: 'Espérant offrir une vie plus équilibrée à sa famille, Lenny quitte l’hystérie de Hollywood et revient s’installer dans la petite ville où il a grandi. Pourtant, entre ses anciens amis, leurs enfants, les grandes brutes et les petits excités, les chauffeurs de bus fous, les flics bourrés à skis et les 400 invités d’une fête costumée complètement déchaînés, il va vite découvrir que même si vous fuyez la folie, parfois, elle vous poursuit où que vous alliez…',
    //   choice1: 'Copains pour toujours 2',
    //   choice2: 'Le Roi lion 2 : L\'Honneur de la tribu',
    //   choice3: 'Les 4 Fantastiques',
    //   choice4: 'Graffiti party',
    //   answer: 'Copains pour toujours 2',
    //   optionalImage: ''
    // }
  ];

  getAllQuestions(): Observable<Question[]> {
    const options = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*'
      })
    }
    return this.http.get<Question[]>("http://localhost:8080/cinequiz/questions/movie/random", options);
  }

  getQuestionByNumber(/*questionNb: number*/): Observable<Question> {
    // const question = this.questions.find(question => question.questionNumber === questionNb);
    // if (!question) {
    //   throw new Error('Question not found');
    // } else {
    //   return question;
    // }
    const options = { headers: new HttpHeaders({ 
      'Access-Control-Allow-Origin':'Content-Type, Authorization'
    })
    }
    return this.http.get<Question>("http://localhost:8080/cinequiz/questions/movie/random",options);
  }

}