import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '../../environments/environment';
import { User } from '../models/user.models';

@Injectable({ providedIn: 'root' })
export class AccountService {
    private userSubject: BehaviorSubject<User>;
    public user: Observable<User>;

    constructor(private router: Router, private http: HttpClient) {
        this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user') || '{}'));
        this.user = this.userSubject.asObservable();
    }

    public get userValue(): User {
        return this.userSubject.value;
    }

    public getUser(){
        return localStorage.getItem('user');
    }

    get isLoggedIn() : boolean{
        return this.getUser() !== null ? true : false;
    }

    login(userEntered:any) { // OK
        return this.http.post<User>(`${environment.apiUrl}/connection`, userEntered).subscribe({
            next : user => {localStorage.setItem('user',JSON.stringify(user));
            console.log(localStorage.getItem('user'));
            this.userSubject.next(user);
            this.router.navigateByUrl("/");
            return user;
        },
            error : (err) => alert("User not found")

        });
    }

    logout() { // OK
        localStorage.removeItem('user');
        this.userSubject.next(null!);
        this.router.navigateByUrl("/");
    }

    register(user: any) { // OK
        this.http.post<User>(`${environment.apiUrl}/add-user`, user)
      .subscribe(result => {
        console.log(result);
        setTimeout(() => { this.router.navigateByUrl("/"); }, 1000);
      });
    }

    getAll() { // OK
        return this.http.get<User[]>(`${environment.apiUrl}/find-all-users`);
    }

    getById(id: string) { // OK
        return this.http.get<User>(`${environment.apiUrl}/find-user/${id}`);
    }

    update(id:string, user:any) { // OK
        return this.http.put<User>(`${environment.apiUrl}/add-user/`, user)
            .pipe(map(x => {
                if (id == this.userValue.id) {
                    // const user = { ...this.userValue, ...user };
                    localStorage.setItem('user', JSON.stringify(user));
                    this.userSubject.next(user);
                }
                return x;
            }));
    }

    delete(id: string) { // OK
        return this.http.delete<User>(`${environment.apiUrl}/delete-user/${id}`)
            .pipe(map(x => {
                if (id == this.userValue.id) {
                    this.logout();
                }
                return x;
            }));
    }
}