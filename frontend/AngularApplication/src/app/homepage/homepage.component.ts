import { Component } from '@angular/core';
import { GameService } from '../services/game.service';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {
    constructor(private game: GameService){
      if(this.game.gameLaunched){
        this.game.reset();
      }
    }
}
