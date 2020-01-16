import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ParseComponent } from './parse/parse.component';
import { CabinetComponent } from './cabinet/cabinet.component';
import { TextAndWordsUnitDetailComponent } from './text-and-words-unit-detail/text-and-words-unit-detail.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
{ path: '', component: HomeComponent },
{ path: 'parse', component: ParseComponent },
{ path: 'cabinet', component: CabinetComponent },
{ path: 'cabinet/:name', component: TextAndWordsUnitDetailComponent },
{ path: 'login', component: LoginComponent },
{ path: 'registration', component: RegistrationComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
