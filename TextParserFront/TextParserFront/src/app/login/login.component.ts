import { Component } from '@angular/core';
import { AppService } from '../app-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {

  credentials = {username: '', password: ''};
  errorsFromServer:string = '';

  constructor(private app: AppService, private router: Router) {
  }

  login() {
      this.app.authenticate(this.credentials, 
                            (success:boolean, errorsFromServer:string) => {
                                    if(success) {
                                        this.router.navigateByUrl('/parse');
                                    } else {
                                        this.errorsFromServer = errorsFromServer;
                                    }
                            });
  }

}