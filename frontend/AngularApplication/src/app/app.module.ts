import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { QuestionComponent } from './question/question.component';
import { HomepageComponent } from './homepage/homepage.component';
import { QuestionListComponent } from './question-list/question-list.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { AppRoutingModule } from './app-routing.module';
import { SingleQuestionComponent } from './single-question/single-question.component';

@NgModule({
  declarations: [
    AppComponent,
    QuestionComponent,
    HomepageComponent,
    QuestionListComponent,
    HeaderComponent,
    FooterComponent,
    SingleQuestionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
