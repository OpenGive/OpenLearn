import {Injectable} from '@angular/core';
import * as _ from "lodash";

@Injectable()
export class PasswordService {
  constructor() {}

  validCharacters = 'abcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+';

  generatePassword(): string {
    const chars = new Array(100);
    for (let i = 0; i < 100; i++) {
      chars[i] = _.sample(this.validCharacters);
    }
    return chars.join("");
  }

}
