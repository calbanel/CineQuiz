import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  ngOnInit() {
    
  }
  onCreation(){
    // let body = document.getElementById('body') as HTMLElement;
    // body.innerHTML = '<app-question [quest]="myQuest"></app-question>';
  }
}
