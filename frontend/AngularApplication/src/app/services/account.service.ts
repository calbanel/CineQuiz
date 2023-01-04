import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '../../environments/environment';
import { User } from '../models/user.models';
import Swal from 'sweetalert2';

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

    public getUser() {
        return localStorage.getItem('user');
    }

    get isLoggedIn(): boolean {
        return this.getUser() !== null ? true : false;
    }

    login(userEntered: any) { 
        return this.http.post<User>(`${environment.apiUrl}/connection`, userEntered).subscribe({
            next: user => {
                localStorage.setItem('user', JSON.stringify(user));
                this.userSubject.next(user);
                Swal.fire('Connecté!','','success')
                this.router.navigateByUrl("/");
                return user;
            },
            error: (err) => Swal.fire('Utilisateur inconnu','Veuillez créer un compte avant de vous connecter','error')

        });
    }

    logout() { 
        localStorage.removeItem('user');
        this.userSubject.next(null!);
        this.router.navigateByUrl("/");
    }

    register(user: any) {
        this.http.post<User>(`${environment.apiUrl}/add-user`, user)
            .subscribe({ next: result => {
                Swal.fire('Compte créé !','','success')
                setTimeout(() => { this.router.navigateByUrl("/"); }, 1000);
            },
            error: (err) => Swal.fire('Email déjà utilisé','Veuillez utiliser une autre adresse mail','error')
        });
    }

    getAll() { 
        return this.http.get<User[]>(`${environment.apiUrl}/find-all-users`);
    }

    getById(id: string) {
        return this.http.get<User>(`${environment.apiUrl}/find-user/${id}`);
    }

    update(id: string, user: any) {
        return this.http.put<User>(`${environment.apiUrl}/add-user/`, user)
            .pipe(map(x => {
                if (id == this.userValue.id) {
                    localStorage.setItem('user', JSON.stringify(user));
                    this.userSubject.next(user);
                }
                return x;
            }));
    }

    delete(id: string) { 
        return this.http.delete<User>(`${environment.apiUrl}/delete-user/${id}`)
            .pipe(map(x => {
                if (id == this.userValue.id) {
                    this.logout();
                }
                return x;
            }));
    }
}