import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserRegistrationForm } from '../user-registration-form';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})

export class RegistrationComponent implements OnInit {
  userRegistrationForm: UserRegistrationForm;
  errorsFromServer: Map<string, string>;

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit() {
     this.userRegistrationForm = new UserRegistrationForm();
     this.errorsFromServer = new Map();
  }
  
  register() {
      this.http.post('http://resttextparser.us-east-2.elasticbeanstalk.com/user/register', JSON.stringify(this.userRegistrationForm)
                      ).subscribe(
                              response => {
                                  this.router.navigateByUrl('/login');
                              },
                              error => {
                                  this.registrationErrorHandler(error.error);
                              }
                      );
  }
   
  registrationErrorHandler(errorObject: Object) {
       this.errorsFromServer = new Map();
       Object.entries(errorObject).forEach(
               ([key, value]) => this.errorsFromServer.set(key, value));
   }

}
