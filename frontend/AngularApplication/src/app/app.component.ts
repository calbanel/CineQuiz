import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'CineQuiz';

  ngOnInit(){
    let page = document.getElementById("contenu") as HTMLElement;
    page.innerHTML = '<app-homepage></app-homepage>';
  }

}