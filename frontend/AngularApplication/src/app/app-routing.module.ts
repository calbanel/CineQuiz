import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomepageComponent } from "./homepage/homepage.component";
import { RegistrationComponent } from "./registration/registration.component";
import { SingleQuestionComponent } from "./single-question/single-question.component";
import { ConnectionComponent } from "./connection/connection.component";

const routes : Routes = [
    {path: 'connection', component: ConnectionComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'questions/:id', component: SingleQuestionComponent},
    {path: '', component: HomepageComponent},
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes)
    ],
    exports:[
        RouterModule
    ]
})
export class AppRoutingModule{

}