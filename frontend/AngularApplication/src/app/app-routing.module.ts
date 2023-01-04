import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomepageComponent } from "./homepage/homepage.component";
import { RegistrationComponent } from "./registration/registration.component";
import { SingleQuestionComponent } from "./single-question/single-question.component";
import { ConnectionComponent } from "./connection/connection.component";
import { CreditsComponent } from "./credits/credits.component";
import { AuthGuard } from "./services/auth.guard";
import { LoadingGameComponent } from "./loading-game/loading-game.component";

const routes : Routes = [
    {path: 'login', component: ConnectionComponent},
    {path: 'sign-in', component: RegistrationComponent},
    {path: 'questions/:id', component: SingleQuestionComponent, canActivate: [AuthGuard]},
    {path: 'loading', component: LoadingGameComponent, canActivate: [AuthGuard]},
    {path: 'credits', component: CreditsComponent},
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