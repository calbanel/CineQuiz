import { Component, OnInit } from '@angular/core';
import { Question } from './models/question.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  questions! :Question[];

  ngOnInit(){
    this.questions = [
      {
        questionNumber: 1,
        type: 'wich-by-image',
        question: 'Quel est ce film ?',
        description: '',
        answerA: 'Un homme d’exception' ,
        answerB:'Invincible : Le chemin de la rédemption' ,
        answerC:'21 grammes',
        answerD: 'Sissi Impératrice',
        answer: 'answerB',
        media: 'https://image.tmdb.org/t/p/w500/6nh02WC31a5Sg3HqK21JY1nHqFo.jpg'
      },
      {
        questionNumber: 2,
        type: 'budget',
        question: 'Quel a été le revenu généré par ce film ?',
        description: 'Solo: A Star Wars Story',
        answerA: '817400891' ,
        answerB:'1850000' ,
        answerC:'121214377',
        answerD: '392952373',
        answer: 'answerD',
        media: 'https://image.tmdb.org/t/p/w500/ojHCeDULAkQK25700fhRU75Tur2.jpg'
      },
      {
        questionNumber: 3,
        type: 'doesnt-take-part',
        question: 'Quelle personne n\'a pas participé à ce film ?',
        description: 'Star Wars : Les Derniers Jedi',
        answerA: 'Kate Dickie' ,
        answerB:'Whoopi Goldberg' ,
        answerC:'Gwendoline Christie',
        answerD: 'Daisy Ridley',
        answer: 'answerB',
        media: 'https://image.tmdb.org/t/p/w500/5Iw7zQTHVRBOYpA0V6z0yypOPZh.jpg'
      },
      {
        questionNumber: 4,
        type: 'release-date',
        question: 'A quelle date est sortie ce film ?' ,
        description: 'Looop Lapeta : La boucle infernale',
        answerA: '2006-05-10' ,
        answerB:'2022-02-04' ,
        answerC:'1999-04-16',
        answerD: '2002-10-18',
        answer: 'answerB',
        media: 'https://image.tmdb.org/t/p/w500/kQM7o3NIkruIZLoQ9E2XzZQ8Ujl.jpg'
      },
      {
        questionNumber: 5,
        type: 'revenue',
        question: 'Quel a été le revenu généré par ce film ?',
        description: 'G.I. Joe : Le Réveil du Cobra',
        answerA: '77000000' ,
        answerB:'16951798' ,
        answerC:'302469017',
        answerD: '26000000',
        answer: 'answerC',
        media: 'https://image.tmdb.org/t/p/w500/vBV2nF3yqYrskq2y1bSSuAuGqcz.jpg'
      },
      {
        questionNumber: 6,
        type: 'take-part',
        question: 'Quelle personne a participé à ce film ?',
        description: 'Superman',
        answerA: 'Eric Lloyd' ,
        answerB:'Terence Stamp' ,
        answerC:'Alan Arkin',
        answerD: 'Tim Allen',
        answer: 'answerB',
        media: 'https://image.tmdb.org/t/p/w500/v6MVBFnQOscITvmAy5N5ras2JKZ.jpg'
      },
      {
        questionNumber: 7,
        type: 'which-by-description',
        question: 'A quel film correspond a cette description ?',
        description: 'Espérant offrir une vie plus équilibrée à sa famille, Lenny quitte l’hystérie de Hollywood et revient s’installer dans la petite ville où il a grandi. Pourtant, entre ses anciens amis, leurs enfants, les grandes brutes et les petits excités, les chauffeurs de bus fous, les flics bourrés à skis et les 400 invités d’une fête costumée complètement déchaînés, il va vite découvrir que même si vous fuyez la folie, parfois, elle vous poursuit où que vous alliez…',
        answerA: 'Copains pour toujours 2' ,
        answerB:'Le Roi lion 2 : L\'Honneur de la tribu',
        answerC:'Les 4 Fantastiques',
        answerD: 'Graffiti party',
        answer: 'answerA',
        media: ''
      }
    ]
  }
}