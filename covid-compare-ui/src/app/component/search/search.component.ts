import { Component, OnInit } from '@angular/core';
import { CountryService } from '../../service/country.service';
import { CountryChartDataService } from '../../service/country-chart-data.service';
import { Country } from '../../model/country';
import { DateValidation } from '../../validation/date-validator';
import * as moment from 'moment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  selectedCountries = [];
  startDate: Date;
  endDate: Date;
  countries: Country[];
  chartForm: FormGroup;
  submitted = false;
  openAccordion = true;

  constructor(private countryService: CountryService, private countryChartDataService: CountryChartDataService,
    private formBuilder: FormBuilder) {
    this.startDate = moment().subtract(7, 'days').toDate();//.format('MM/DD/YYYY');
    this.endDate = moment().toDate();//.format('MM/DD/YYYY');
  }

  ngOnInit(): void {

    this.chartForm = this.formBuilder.group({
      countriesSelect: [this.selectedCountries, Validators.required],
      startDate: [null, Validators.required],
      endDate: [null, Validators.required]
    },
      { validators: [DateValidation.StartDateBeforeEndDate, DateValidation.EndDateInFuture] }
    );
    this.countryService.getCountries().subscribe(response => {
      this.countries = response;
    });
  }

  get f() {
    return this.chartForm.controls;
  }

  doSubmit() {
    this.submitted = true;

    if (this.chartForm.invalid) {
      return;
    }
    this.openAccordion = false;

    this.selectedCountries = this.f.countriesSelect.value;
    this.startDate = this.f.startDate.value;
    this.endDate = this.f.endDate.value;
    this.countryChartDataService.reloadChart();
  }


}
