import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Country} from '../model/country';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';

const BASE_URL = environment.BACKEND_HOST + '/api/v1/covid/';
const COVID_URL = {
  COUNTRIES: `${BASE_URL}countries`,
};

@Injectable({
  providedIn: 'root'
})
export class CountryService {

  constructor(private http: HttpClient) {
  }

  getCountries(): Observable<Country[]> {
    return this.http.get<Country[]>(COVID_URL.COUNTRIES);
  }

}
