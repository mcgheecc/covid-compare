import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CountryChartData} from '../model/country-chart-data';
import {Observable, Subject} from 'rxjs';
import {environment} from '../../environments/environment';

const BASE_URL = environment.BACKEND_HOST + '/api/v1/covid/';
const COVID_URL = {
  DATA: (countryCodes: string, startDate: string, endDate: string) => `${BASE_URL}data/${countryCodes}/${startDate}/${endDate}`, //2020-03-25
};

@Injectable({
  providedIn: 'root'
})
export class CountryChartDataService {

  private reloadChartSource = new Subject<string>();

  constructor(private http: HttpClient) {
  }

  getData(countryCodes: string, startDate: string, endDate: string): Observable<CountryChartData[]> {
    return this.http.get<CountryChartData[]>(`${COVID_URL.DATA(countryCodes, startDate, endDate)}`);
  }

  shouldReloadChart(): Observable<any> {
    return this.reloadChartSource.asObservable();
  }

  reloadChart() {
    this.reloadChartSource.next()
  }

}
