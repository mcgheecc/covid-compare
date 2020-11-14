import { Component, Input, OnInit } from '@angular/core';
import * as moment from 'moment';
import { CountryChartData } from 'src/app/model/country-chart-data';
import { SeriesData } from 'src/app/model/series-data';
import { CountryChartDataService } from 'src/app/service/country-chart-data.service';

@Component({
  selector: 'deaths-chart',
  templateUrl: './death-chart.component.html',
  styleUrls: ['./death-chart.component.css']
})
export class DeathChartComponent implements OnInit {

  @Input()
  selectedCountries: string[];
  @Input()
  startDate: Date;
  @Input()
  endDate: Date;

  chartType = 'bar';
  isLoadingDeaths = false;
  countryDeathsChartOptions: any;
  countryChartData: CountryChartData[];

  deathsLoadingOpts = {
    text: 'loading',
    color: '#000',
    textColor: '#000',
    maskColor: 'rgba(255, 255, 255, 0.8)',
    zlevel: 0
  };

  constructor(private countryChartDataService: CountryChartDataService) { }

  ngOnInit(): void {
    this.countryChartDataService.shouldReloadChart().subscribe(() => {
      this.getDeathsByCountry();
    });
  }

  getDeathsByCountry() {
    const startDate = moment(this.startDate).format('YYYY-MM-DD');
    const endDate = moment(this.endDate).format('YYYY-MM-DD');
    this.isLoadingDeaths = true;
    this.countryChartDataService.getData(this.selectedCountries.join(), startDate, endDate).subscribe((response => {
      this.countryChartData = response;
      this.setOptions(this.countryChartData);
    }));
  }

  toggleChartType() {
    this.chartType = this.chartType === 'bar' ? 'line' : 'bar';
    this.setOptions(this.countryChartData);
  }

  setOptions(data: CountryChartData[]) {
    const xAxisData = [];
    const xAxisDeathsData = [];
    const legendData = [];
    const deathsSeries = [];

    if (data && data.length >= 1 && data[0].cases) {
      data[0].deaths.sort((a,b) => (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0));
      data[0].deaths = data[0].deaths.filter((v,i,a)=>a.findIndex(t=>(t.name === v.name))===i);

      for (let j = 0; j < data[0].cases.length; j++) {
        xAxisData.push(data[0].cases[j].name);
      }

      for (let j = 0; j < data[0].deaths.length; j++) {
        xAxisDeathsData.push(data[0].deaths[j].name);
      }

      for (let i = 0; i < data.length; i++) {
        legendData.push(data[i].name);

        let deaths = data[i].deaths;
        const deathsSeriesData: SeriesData = {
          name: data[i].name,
          type: this.chartType,
          data: deaths
        };
        deathsSeries.push(deathsSeriesData);
      }

      xAxisData.sort();
      xAxisDeathsData.sort();
    }
    this.isLoadingDeaths = false;

    this.countryDeathsChartOptions = {
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "shadow"
        }
      },
      legend: {
        data: legendData,
        align: 'left',
      },
      yAxis: {},
      series: deathsSeries,
      xAxis: {
        show: true,
        data: xAxisDeathsData,
      },
    };
  }

}
