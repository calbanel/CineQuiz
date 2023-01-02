import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { User } from "../models/user.models";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {

  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/add-user`,user);
  }

}