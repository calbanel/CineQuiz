import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Game } from '../models/game.models';
import { AccountService } from '../services/account.service';
import { GameService } from '../services/game.service';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent{
    gameHistory !: Game[];
    selectedGame ?: Game;

    constructor(private user: AccountService, private gameService : GameService, private router: Router){
      if(this.gameService.gameLaunched){
        this.gameService.reset();
      }

      user.getById(user.userValue.id).subscribe(value => {
        this.gameHistory = value.games;
        this.gameHistory = this.gameHistory.reverse();
      });
    }

    loading(){
      this.router.navigateByUrl("/loading");
    }

    changeGame(e: any){
      if(e.target.value == "null")
        this.selectedGame = undefined;
      else
        this.selectedGame = this.gameHistory.find(value => value.date === e.target.value);

      let carousel : any = document.getElementById("carousel-1");
      if(carousel != null)
        carousel.checked = true;
    }
}
