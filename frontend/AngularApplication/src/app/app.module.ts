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
import { ReactiveFormsModule } from '@angular/forms';
import { RegistrationComponent } from './registration/registration.component';
import { ConnectionComponent } from './connection/connection.component';

@NgModule({
  declarations: [
    AppComponent,
    QuestionComponent,
    HomepageComponent,
    QuestionListComponent,
    HeaderComponent,
    FooterComponent,
    SingleQuestionComponent,
    RegistrationComponent,
    ConnectionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
