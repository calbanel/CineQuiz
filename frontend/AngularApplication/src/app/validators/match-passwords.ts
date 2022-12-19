import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function matchPasswordsValidator(password:string, confirm:string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.get(password) || !control.get(confirm)) {
            return {
                confirmEqual: 'Invalid control names'
            };
        }
        const mainValue = control.get(password)!.value;
        const confirmValue = control.get(confirm)!.value;
        
        return mainValue === confirmValue ? null : {
            confirmEqual: {
                main: mainValue,
                confirm: confirmValue
            }
        };
    };
}