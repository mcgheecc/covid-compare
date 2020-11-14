import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { BatchJobResponse } from '../model/batch-job-response';
import { environment } from '../../environments/environment'

const BATCH_JOB_URL = {
  UPDATE_COUNTRIES_JOB: environment.UPDATE_COUNTRIES_JOB_HOST + '/job/start/updateCountriesJob',
  UPDATE_COUNTRY_DATA_JOB: (startDate: string, endDate: string) => environment.UPDATE_COUNTRY_DATA_JOB_HOST + `/job/start/updateCountryDataJob/${startDate}/${endDate}`,
  UPDATE_COUNTRY_DAILY_DATA_JOB: environment.UPDATE_COUNTRY_DAILY_DATA_JOB_HOST + '/job/start/updateDailyCountryDataJob'
};
@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  private showAdminModal = new Subject<string>();

  shouldShowAdminModal(): Observable<any> {
    return this.showAdminModal.asObservable();
  }

  showAdmin() {
    this.showAdminModal.next()
  }

  startUpdateCountriesJob() {
    const url = BATCH_JOB_URL.UPDATE_COUNTRIES_JOB;
    return this.http.get<BatchJobResponse>(url);
  }

  startUpdateCountryDataJob(startDate: string, endDate: string): Observable<BatchJobResponse> {
    const url = `${BATCH_JOB_URL.UPDATE_COUNTRY_DATA_JOB(startDate, endDate)}`;
    return this.http.get<BatchJobResponse>(url);
  }

  startUpdateCountryDailyDataJob(): Observable<BatchJobResponse> {
    const url = BATCH_JOB_URL.UPDATE_COUNTRY_DAILY_DATA_JOB;
    return this.http.get<BatchJobResponse>(url);
  }
}
