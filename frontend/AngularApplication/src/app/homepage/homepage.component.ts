import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { GameService } from '../services/game.service';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {
    constructor(private game: GameService, private router: Router){
      if(this.game.gameLaunched){
        this.game.reset();
      }
    }

    loading(){
      this.router.navigateByUrl("/loading");
    }
}
