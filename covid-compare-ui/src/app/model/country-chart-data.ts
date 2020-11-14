import {NameValue} from './name-value';

export interface CountryChartData {
    name: string,
    cases: NameValue[],
    casesPerHundredThousand: NameValue[],
    deaths: NameValue[],
    type: 'line'
  }