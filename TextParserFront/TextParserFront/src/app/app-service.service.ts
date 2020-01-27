import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import * as moment from 'moment';

@Injectable()
export class AppService {
  authenticated: boolean;

  constructor(private http: HttpClient, private router: Router ) {
      if (localStorage.getItem('auth_token') == null) {
          this.authenticated = false;
      }
  }

  authenticate(credentials, callback) {
        this.http.post('http://resttextparser.us-east-2.elasticbeanstalk.com/user/login', credentials)
            .subscribe(
                    response => {
                            this.setSession(response);
                            return callback(true, '');
                    },
                    error => {
                            return callback(false, error.error);
                    }
                );
    }
  
  private setSession(authResult) {
      const expiresAt = moment(authResult.expiresAt);
      localStorage.setItem('auth_token', authResult.token);
      localStorage.setItem("expiresAt", JSON.stringify(expiresAt.valueOf()));
      this.authenticated = true;
  }       
  
  public logout(){
          localStorage.removeItem("auth_token");
          localStorage.removeItem("expiresAt");
          this.authenticated = false;
          this.router.navigateByUrl('/login');
    }
  
  public isTokenExpired() {
      return !moment().isBefore(this.getExpiration());
      
  }
  
  getExpiration() {
      const expiration = localStorage.getItem("expiresAt");
      const expiresAt = JSON.parse(expiration);
      return moment(expiresAt);
  }    
 
}