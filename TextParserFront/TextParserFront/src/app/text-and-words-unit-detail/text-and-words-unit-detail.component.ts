import { Component, OnInit, Input, OnChanges, SimpleChanges, SimpleChange } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TextAndParsedWordsUnit } from '../text-and-parsed-words-unit';


@Component({
  selector: 'app-text-and-words-unit-detail',
  templateUrl: './text-and-words-unit-detail.component.html',
  styleUrls: ['./text-and-words-unit-detail.component.scss']
})

export class TextAndWordsUnitDetailComponent implements OnInit {
    currentUnitName: string;
    newUnitName: string;
    currentTextAndParsedWordsUnit: TextAndParsedWordsUnit;
    changeNameButtonIsClicked: boolean  = false;
    changeAllButtonIsClicked: boolean = false;
    selectedWordsForDelete: Array<string>;
    errorOnChange: string;
        
  constructor(private http: HttpClient,
          private route: ActivatedRoute,
          private router: Router,
          private location: Location        
  ) {}
  
  ngOnInit(): void {
      this.currentUnitName = this.route.snapshot.paramMap.get('name');
      this.errorOnChange = "";
      this.selectedWordsForDelete = new Array();
      this.getCurrentTextAndParsedWordsUnit();
  }
  
  getCurrentTextAndParsedWordsUnit(): void {
      this.http.get('http://localhost:8080/cabinet/all/' + this.currentUnitName)
                          .subscribe(response => this.currentTextAndParsedWordsUnit = new TextAndParsedWordsUnit(response));
  }
  
  showChangeNameForm(): void {
      this.changeNameButtonIsClicked = true;
      this.changeAllButtonIsClicked = false;
      this.newUnitName = this.currentTextAndParsedWordsUnit.name;
  }
  
  changeName():void {
      this.http.patch('http://localhost:8080/cabinet/' + this.currentUnitName, this.newUnitName)
                      .subscribe(
                              success=>{
                                  this.router.navigateByUrl('/cabinet');
                              },
                              (error: HttpErrorResponse) => {
                                  this.errorOnChange = error.error;
                              }
                      );
  }
  
  showChangeAllForm(): void {
      this.changeAllButtonIsClicked = true;
      this.changeNameButtonIsClicked = false;
      this.newUnitName = this.currentTextAndParsedWordsUnit.name;
  }
  
  changeAll():void {
      var newTextAndParsedWordsUnit = this.prepareNewTextAndParsedWordsUnitFromCurrentOne(this.currentTextAndParsedWordsUnit);
      this.http.put('http://localhost:8080/cabinet/' + this.currentUnitName, newTextAndParsedWordsUnit)
                      .subscribe(
                              (success)=>{
                                  this.router.navigateByUrl('/cabinet');
                              },
                              (error: HttpErrorResponse)=>{
                                  this.errorOnChange = error.error;
                              }
                      );
  }
  
  prepareNewTextAndParsedWordsUnitFromCurrentOne(currentTextAndParsedWordsUnit: TextAndParsedWordsUnit):TextAndParsedWordsUnit {
      var newTextAndParsedWordsUnit = new TextAndParsedWordsUnit(currentTextAndParsedWordsUnit);
      newTextAndParsedWordsUnit.parsedWords = currentTextAndParsedWordsUnit.getParsedWords();
      newTextAndParsedWordsUnit.name = this.newUnitName;
      this.deleteChoosenWordsFromNewTextAndParsedWordsUnit(this.selectedWordsForDelete, newTextAndParsedWordsUnit);
      newTextAndParsedWordsUnit.convertParsedWordsMapToObject();
      JSON.stringify(newTextAndParsedWordsUnit);
      return newTextAndParsedWordsUnit;
  }
  
  deleteChoosenWordsFromNewTextAndParsedWordsUnit(selectedWordsForDelete: Array<string>, newTextAndParsedWordsUnit: TextAndParsedWordsUnit) {
      selectedWordsForDelete.forEach((value) => {
          if (newTextAndParsedWordsUnit.getParsedWords().has(value)){
              newTextAndParsedWordsUnit.getParsedWords().delete(value);
          }
      })
  }
 
  addToDeleteList(isChecked:boolean, word:string){
      if (isChecked) {
          this.selectedWordsForDelete.push(word);
      }
      //user can check and then uncheck box, so for such cases was created step below 
      else if (!isChecked) {
          this.selectedWordsForDelete.splice(this.selectedWordsForDelete.indexOf(word), 1);
      }
  }
  
  goBack(): void {
      this.location.back();
  }
 
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
