import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomepageComponent } from "./homepage/homepage.component";
import { RegistrationComponent } from "./registration/registration.component";
import { SingleQuestionComponent } from "./single-question/single-question.component";
import { ConnectionComponent } from "./connection/connection.component";
import { RankingComponent } from "./ranking/ranking.component";
import { LobbyComponent } from "./lobby/lobby.component";
import { AuthGuard } from "./services/auth.guard";

const routes : Routes = [
    {path: 'lobby', component: LobbyComponent, canActivate: [AuthGuard]},
    {path: 'ranking', component: RankingComponent, canActivate: [AuthGuard]},
    {path: 'login', component: ConnectionComponent},
    {path: 'sign-in', component: RegistrationComponent},
    {path: 'questions/:id', component: SingleQuestionComponent, canActivate: [AuthGuard]},
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