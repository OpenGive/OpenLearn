import {FormGroup, AbstractControl} from '@angular/forms';

export function FourteenPlusValidator(emailKey: string, fourteenPlusKey: string) {
  return (group: FormGroup): {[key: string]: any} => {
    const email = group.controls[emailKey];
    const fourteenPlus = group.controls[fourteenPlusKey];
    if (email.value !== null && email.value !== "" && !(fourteenPlus.value)) {
      return {
        restrictEmail: true
      };
    }
    return null;
  }
}
