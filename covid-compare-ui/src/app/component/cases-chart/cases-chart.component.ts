import { Component, Input, OnInit } from '@angular/core';
import { CountryChartDataService } from '../../service/country-chart-data.service';
import { CountryChartData } from '../../model/country-chart-data';
import { SeriesData } from '../../model/series-data';
import * as moment from 'moment';

@Component({
  selector: 'cases-chart',
  templateUrl: './cases-chart.component.html',
  styleUrls: ['./cases-chart.component.css']
})
export class CasesChartComponent implements OnInit {

  @Input()
  selectedCountries: string[];
  @Input()
  startDate: Date;
  @Input()
  endDate: Date;

  chartType = 'bar';
  isLoadingCases = false;
  countryCasesChartOptions: any;
  showCasesPerHundredThousand = false;
  countryChartData: CountryChartData[];

  casesLoadingOpts = {
    text: 'loading',
    color: '#000',
    textColor: '#000',
    maskColor: 'rgba(255, 255, 255, 0.8)',
    zlevel: 0
  };

  constructor(private countryChartDataService: CountryChartDataService) { }

  getCasesByCountry(): any {
    const startDate = moment(this.startDate).format('YYYY-MM-DD');
    const endDate = moment(this.endDate).format('YYYY-MM-DD');
    this.isLoadingCases = true;
    this.countryChartDataService.getData(this.selectedCountries.join(), startDate, endDate).subscribe((response => {
      this.countryChartData = response;
      this.setOptions(response);
    }));
  }

  toggleChartType() {
    this.chartType = this.chartType === 'bar' ? 'line' : 'bar';
    this.setOptions(this.countryChartData);
  }

  toggleCasesType() {
    this.showCasesPerHundredThousand = !this.showCasesPerHundredThousand;
    this.setOptions(this.countryChartData);
  }

  ngOnInit(): void {
    this.countryChartDataService.shouldReloadChart().subscribe(() => {
      this.getCasesByCountry();
    });
  }

  public setOptions(data: CountryChartData[]) {
    const xAxisData = [];
    const legendData = [];
    const casesSeries = [];

    if (data && data.length >= 1 && data[0].cases) {
      data[0].cases.sort((a,b) => (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0));
      data[0].cases = data[0].cases.filter((v,i,a)=>a.findIndex(t=>(t.name === v.name))===i);
      data[0].casesPerHundredThousand.sort((a,b) => (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0));
      data[0].casesPerHundredThousand = data[0].casesPerHundredThousand.filter((v,i,a)=>a.findIndex(t=>(t.name === v.name))===i);

      for (let j = 0; j < data[0].cases.length; j++) {
        xAxisData.push(data[0].cases[j].name);
      }

      for (let i = 0; i < data.length; i++) {
        legendData.push(data[i].name);

        let cases = data[i].cases;
        if (this.showCasesPerHundredThousand) {
          cases = data[i].casesPerHundredThousand;
        }

        const casesSeriesData: SeriesData = {
          name: data[i].name,
          type: this.chartType,
          data: cases
        };
        casesSeries.push(casesSeriesData);
      }

      xAxisData.sort();
    }
    this.isLoadingCases = false;

    this.countryCasesChartOptions = this.getOptions(legendData, casesSeries, xAxisData);
  }

  getOptions(legendData, casesSeries, xAxisData) {
    return {
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "shadow"
        }
      },
      legend: {
        data: legendData,
        align: 'left'
      },
      yAxis: {},
      series: casesSeries,
      xAxis: {
        show: true,
        data: xAxisData
      },
    };
  }

}
