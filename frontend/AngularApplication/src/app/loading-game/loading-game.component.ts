import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import Swal from 'sweetalert2';
import { Question } from '../models/question.model';
import { GameService } from '../services/game.service';


@Component({
  selector: 'app-loading-game',
  templateUrl: './loading-game.component.html',
  styleUrls: ['./loading-game.component.css']
})
export class LoadingGameComponent implements OnInit, OnDestroy {
  questions !: Question[];

  constructor(private game: GameService, private router: Router) {

    if(game.gameLaunched)
      game.reset();
    
    game.generateQuiz(environment.nbQuestionsInQuiz).subscribe(
    {
      next: (val) => {
        if(val)
          this.router.navigateByUrl(`/questions/1`);
        else
          Swal.fire('Echec de la création','Veuillez réessayer','error')
      },
      error: (err) => Swal.fire('Impossible de créer une partie','Veuillez réessayer ultérieurement','error')
    });

  }

  ngOnDestroy() {
  
  }

  ngOnInit() {
    
  }
}
