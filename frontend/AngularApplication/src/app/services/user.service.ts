import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { User } from "../models/user.models";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {

  }

  addUser(user: User): Observable<User> {
    const options = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': 'Content-Type, Authorization'
      })
    }
    return this.http.post<User>("http://localhost:8080/cinequiz/users/addUser", user, options);
  }

}