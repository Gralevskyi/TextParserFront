import { Component, OnInit } from '@angular/core';
import { AppService } from '../app-service.service';



@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
   
  constructor(private app: AppService) {
     
  }

  ngOnInit() {
     
  }

}