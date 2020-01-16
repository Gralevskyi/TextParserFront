import { Component, OnInit, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, finalize } from 'rxjs/operators';
import { TextAndParsedWordsUnit } from '../text-and-parsed-words-unit';

@Component({
  selector: 'app-cabinet',
  templateUrl: './cabinet.component.html',
  styleUrls: ['./cabinet.component.css']
})

@Injectable()
export class CabinetComponent implements OnInit {

  savedWordsList = new Array<TextAndParsedWordsUnit>();

  constructor(private http: HttpClient) { }

  ngOnInit() {
      this.savedWordsList = new Array<TextAndParsedWordsUnit>();
      this.http.get('http://localhost:8080/cabinet/all')
          .subscribe((response: any[]) => {
                  response.map(
                          (item: any) => {
                              this.savedWordsList.push(new TextAndParsedWordsUnit(item));
                          }
                  );
          });

  }
  
  delete(wordsListName: string): void {
      this.http.delete('http://localhost:8080/cabinet/' + wordsListName)
                          .pipe(finalize(
                                  () => {this.ngOnInit()}
                                                           )).subscribe();
  }
 
}
