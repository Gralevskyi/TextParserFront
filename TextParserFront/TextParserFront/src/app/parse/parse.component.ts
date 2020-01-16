import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TextAndParsedWordsUnit } from '../text-and-parsed-words-unit';
import { AppService } from '../app-service.service';

@Component({
  selector: 'app-parse',
  templateUrl: './parse.component.html',
  styleUrls: ['./parse.component.scss']
})

export class ParseComponent implements OnInit {

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
  }
  userText: string = '';
  resultOfParsing: Map<string, number>;
  savingErrorMessage: string;
  textSizeErrorMessage: string;
  
  ngOnInit() {
      this.savingErrorMessage ="";
      this.textSizeErrorMessage = "";
  }
  
  parse(userText: string) {
      console.log("Parse method");
      this.resultOfParsing = new Map();
      this.http.post('http://localhost:8080/parse', JSON.stringify(userText))
              .subscribe(
                      (response) => { 
                          Object.entries(response).forEach(
                                  ([key, value]) => this.resultOfParsing.set(key, <number>value));
                          this.textSizeErrorMessage = "";
                      },
                      (error: HttpErrorResponse)=>{
                          this.resultOfParsing = null;
                          this.textSizeErrorMessage = error.error;
                      });
     }
  
  saveNewTextAndParsedWordsUnit(textName: string): void {
      var newTextAndParsedWordsUnit = this.prepareNewTextAndParsedWordsUnit(textName);
      this.http.post('http://localhost:8080/parse/saveParsedWords', newTextAndParsedWordsUnit)
      .subscribe(
              (response) => {
                          this.router.navigateByUrl('/cabinet');
                      },
              (error: HttpErrorResponse) => {
                          this.savingErrorMessage = error.error;
                      }
              );
  }
  
  prepareNewTextAndParsedWordsUnit(textName: string):TextAndParsedWordsUnit {
      var newTextAndParsedWordsUnit = new TextAndParsedWordsUnit();
      newTextAndParsedWordsUnit.name = textName;
      newTextAndParsedWordsUnit.userText = this.userText;
      this.convertResultOfParsingToObjectAndDefineItAsUnitParsedWords(newTextAndParsedWordsUnit, this.resultOfParsing);
      JSON.stringify(newTextAndParsedWordsUnit);
      return newTextAndParsedWordsUnit;
  }
  
  convertResultOfParsingToObjectAndDefineItAsUnitParsedWords(textAndParsedWordsUnit: TextAndParsedWordsUnit, resultOfParsing: Map<string, number>) {
      //convert Map to object for next converting to json
        let objFromMap = Object.create(null);
        for (let [k,v] of resultOfParsing) {
            objFromMap[k] = v;
        }
        textAndParsedWordsUnit.parsedWords = objFromMap;
    }
  
  authenticated() { return this.app.authenticated; }
  
  sortTable(n) {
      var table, rows, switching, i, x, y, shouldSwitch, comparisonResult, dir, switchcount = 0;
      table = document.getElementById("parsedWordsTable");
      switching = true;
      // Set the sorting direction to ascending:
      dir = "asc";
      /* Make a loop that will continue until
      no switching has been done: */
      while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
          // Start by saying there should be no switching:
          shouldSwitch = false;
          /* Get the two elements you want to compare,
          one from current row and one from the next: */
          x = rows[i].getElementsByTagName("TD")[n];
          y = rows[i + 1].getElementsByTagName("TD")[n];
          /* For different types there is different comparison*/
          
          /* Check if the two rows should switch place,
          based on the direction, asc or desc: */
          if (dir == "asc") {
           if(n == 0) {
               comparisonResult =   x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase(); //Compare of strings
           } else {
               comparisonResult =   Number(x.innerHTML) > (y.innerHTML); //Compare of numbers
           }
            if (comparisonResult) {
              // If so, mark as a switch and break the loop:
              shouldSwitch = true;
              break;
            }
          } else if (dir == "desc") {
              if(n == 0) {
                  comparisonResult =   x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase(); //Compare of strings
              } else {
                  comparisonResult =   Number(x.innerHTML) < (y.innerHTML); //Compare of numbers
              }
              if (comparisonResult) {
              // If so, mark as a switch and break the loop:
              shouldSwitch = true;
              break;
            }
          }
        }
        if (shouldSwitch) {
          /* If a switch has been marked, make the switch
          and mark that a switch has been done: */
          rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
          switching = true;
          // Each time a switch is done, increase this count by 1:
          switchcount ++;
        } else {
          /* If no switching has been done AND the direction is "asc",
          set the direction to "desc" and run the while loop again. */
          if (switchcount == 0 && dir == "asc") {
            dir = "desc";
            switching = true;
          }
        }
      }
    }
 
}

