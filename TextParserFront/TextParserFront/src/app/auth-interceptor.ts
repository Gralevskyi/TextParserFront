import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, EMPTY } from 'rxjs';
import { AppService } from './app-service.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    
    constructor(private app: AppService,  private router: Router) {
    }

    intercept(req: HttpRequest<any>,
              next: HttpHandler): Observable<HttpEvent<any>> {
        console.log("Auth interceptor");
        const idToken = localStorage.getItem("id_token");
        if (idToken) {
            console.log("auth_int if id_token");
            if(this.app.isTokenExpired()){
                this.app.logout();
                this.router.navigateByUrl('/login'); 
                return EMPTY
            } else {
                console.log("auth_int set auth header")
                const cloned = req.clone({
                headers: req.headers.set("Authorization",
                    "Bearer_" + idToken)
                });

            return next.handle(cloned); }
        }
        else {
            console.log("auth_int set acces control allow origin");
            const AllowOriginHeader = req.clone({
                headers: req.headers.set("Access-Control-Allow-Origin", "*")
            })
            return next.handle(AllowOriginHeader);
        }
           
    }
}