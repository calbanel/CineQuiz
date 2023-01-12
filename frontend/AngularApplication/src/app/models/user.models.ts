import { Game } from "./game.models";

export interface User{
    id : string;
    pseudo : string;
    email : string;
    password : string;
    games : Game[];
}