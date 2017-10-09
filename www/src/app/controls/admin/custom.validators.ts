import {FormGroup, AbstractControl} from '@angular/forms';

export function FourteenPlusValidator(emailKey: string, fourteenPlusKey: string) {
	return (group: FormGroup): {[key: string]: any} => {
	    let email = group.controls[emailKey];
	    let fourteenPlus = group.controls[fourteenPlusKey];
	    if (!email.pristine && email.value != "" && !(fourteenPlus.value)) {
	      return {
	        restrictEmail: true
	      };
	    }
	    return null;
	  }
}