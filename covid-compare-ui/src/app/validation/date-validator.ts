import {AbstractControl, ValidationErrors} from '@angular/forms';
import * as moment from 'moment';

export class DateValidation {

  static StartDateBeforeEndDate(control: AbstractControl): ValidationErrors | null {
    if (control.get('startDate') !== null) {
      let sd = control.get('startDate').value;
      let ed = control.get('endDate').value;

      const startDate = moment(sd, 'MM/DD/YYYY');
      const endDate = moment(ed, 'MM/DD/YYYY');

      if (startDate.isAfter(endDate)) {
        control.get('startDate').setErrors({StartDateBeforeEndDate: true})
      } else {
        return null
      }
    }
  }

  static EndDateInFuture(control: AbstractControl): ValidationErrors | null {
    if (control.get('endDate') !== null) {

      let ed = control.get('endDate').value;

      const today = moment();
      const endDate = moment(ed, 'MM/DD/YYYY');

      if (endDate.isAfter(today)) {
        control.get('endDate').setErrors({EndDateInFuture: true})
      } else {
        return null
      }
    }
  }
}
