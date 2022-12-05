import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomepageComponent } from "./homepage/homepage.component";
import { Question } from "./models/question.model";
import { QuestionListComponent } from "./question-list/question-list.component";
import { QuestionComponent } from "./question/question.component";
import { SingleQuestionComponent } from "./single-question/single-question.component";

const routes : Routes = [
    {path: 'questions/:id', component: SingleQuestionComponent},
    {path: 'questions', component: QuestionListComponent},
    {path: '', component: HomepageComponent},
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})
    ],
    exports:[
        RouterModule
    ]
})
export class AppRoutingModule{

}