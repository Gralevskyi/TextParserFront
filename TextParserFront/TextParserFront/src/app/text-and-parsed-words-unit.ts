
export class TextAndParsedWordsUnit {
    
    id: number;
    name: string;
    userText: string;
    parsedWords: Map<string, number>;
    
    constructor(obj?: any) {
       if (obj != null) { 
           this.id = obj.id;
           this.name = obj.name;
           this.userText = obj.userText;
           this.parsedWords = new Map();
           Object.entries(obj.parsedWords).forEach(
                   ([key, value]) => this.parsedWords.set(key, <number>value)
                 );
       }
    }
    
    getParsedWords(): Map<string, number> {
        return this.parsedWords;
    }
    
    convertParsedWordsMapToObject() {
        //convert Map to object for next converting to json
          let objFromMap = Object.create(null);
          for (let [k,v] of this.parsedWords) {
              objFromMap[k] = v;
          }
          this.parsedWords = objFromMap;
      }
    
}