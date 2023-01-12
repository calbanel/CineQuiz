import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function patternValidator(regex: RegExp, error: ValidationErrors): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) { // vide
        return null;
      }
  
      const valid = regex.test(control.value);

      return valid ? null : error;
    };
  }