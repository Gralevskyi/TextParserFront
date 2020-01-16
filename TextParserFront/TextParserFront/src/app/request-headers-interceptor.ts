import { Injectable } from '@angular/core';
import { HttpHeaders, HttpRequest, HttpHandler, HttpInterceptor, HttpEvent } from '@angular/common/http';
import { Observable, EMPTY } from 'rxjs';
import { AppService } from './app-service.service';
import { Router } from '@angular/router';


@Injectable()
export class RequestHeadersInterceptor implements HttpInterceptor {

    constructor(private app: AppService,  private router: Router) {
    }
    
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      
      var headers: HttpHeaders = new HttpHeaders({'X-Requested-With': 'XMLHttpRequest'});
      const AuthToken = localStorage.getItem("auth_token");
      
      if (req.method != 'GET') {
              headers = headers.append('Content-type', 'application/json');
      } 
      
      if (AuthToken) {
          if(this.app.isTokenExpired()){
              this.app.logout();
              this.router.navigateByUrl('/login'); 
              return EMPTY
          } else {
              headers = headers.append("Authorization", "Bearer_" + AuthToken);
         }
      }
      else {
          headers = headers.append("Access-Control-Allow-Origin", "*");
      }
      
      const formedHeaders = req.clone({headers});
      return next.handle(formedHeaders);
  }
}