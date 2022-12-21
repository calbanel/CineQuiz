import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user.models';

@Injectable()
export class AppService {

    authenticated = false;

    constructor(private http: HttpClient) {
    }

    authenticate(credentials:any, callback:any) {

        const headers = new HttpHeaders(credentials ? {
            authorization: 'Basic ' + btoa(credentials.email + ':' + credentials.password)
        } : {});

        this.http.get<User>('user', { headers: headers }).subscribe(response => {
            if (response['pseudo']) {
                this.authenticated = true;
            } else {
                this.authenticated = false;
            }
            return callback && callback();
        });

    }

}