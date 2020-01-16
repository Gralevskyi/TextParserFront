import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { CabinetComponent } from './cabinet/cabinet.component';
import { TextAndWordsUnitDetailComponent } from './text-and-words-unit-detail/text-and-words-unit-detail.component';
import { ParseComponent } from './parse/parse.component';
import { AppRoutingModule } from './app-routing.module';
import { AppService } from './app-service.service';
import { RequestHeadersInterceptor } from './request-headers-interceptor';
import { LoginComponent } from './login/login.component';
import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HTTP_INTERCEPTORS
} from '@angular/common/http';
import { RegistrationComponent } from './registration/registration.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    CabinetComponent,
    TextAndWordsUnitDetailComponent,
    ParseComponent,
    LoginComponent,
    RegistrationComponent,
    NavbarComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [AppService, { provide: HTTP_INTERCEPTORS, useClass: RequestHeadersInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
