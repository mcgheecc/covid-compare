import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/service/admin.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DateValidation } from 'src/app/validation/date-validator';
import { BatchJobStatusService } from 'src/app/service/batch-job-status.service'
import * as moment from 'moment';
import { interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  showAdmin = false;
  clrModalSize = "lg";
  updateCountryDataJobForm: FormGroup;
  response: string;
  loading = false;
  errorMessage: string;
  updateCountryJobStatus: string;
  updateCountryDataJobStatus: string;
  updateDailyCountryDataJobStatus: string;
  disableButtons = environment.DISABLE_ADMIN;
  isDisabled:boolean = environment.DISABLE_ADMIN;

  constructor(private adminService: AdminService, private formBuilder: FormBuilder,
    private batchJobStatusService: BatchJobStatusService ) {
    adminService.shouldShowAdminModal().subscribe(() => this.showAdmin = true);
  }

  ngOnInit(): void {
    this.updateCountryDataJobForm = this.formBuilder.group({
      startDate: [null, Validators.required],
      endDate: [null, Validators.required]
    },
      { validators: [DateValidation.StartDateBeforeEndDate, DateValidation.EndDateInFuture] }
    );
  }

  startUpdateCountriesJob() {
    this.disableButtons = true;
    this.errorMessage = null;
    this.loading = true;
    this.adminService.startUpdateCountriesJob().subscribe(
      (response) => {
        this.response = response.message;
        this.loading = false;
        const sub = interval(3000).pipe(
          switchMap(() => this.batchJobStatusService.getJobStatus('updateCountries')),
        ).subscribe(response => {
          this.updateCountryJobStatus = response.status;
          console.log(status);
          if (response.status === 'COMPLETED' || response.status ==='FAILED') {
            sub.unsubscribe();
            if (response.status === 'FAILED') {
              this.errorMessage = 'updateCountries job failed.'
            }
            this.disableButtons = false;
          }
        });
      },
      (error) => {
        this.errorMessage = error.message;
        this.response = null;
        this.loading = false;
        this.disableButtons = false;
      });

  }

  startUpdateCountryDataJob() {
    this.errorMessage = null;
    this.disableButtons = true;
    if (this.updateCountryDataJobForm.invalid) {
      return;
    }
    this.loading = true;

    const sd = this.updateCountryDataJobForm.controls.startDate.value;
    const ed = this.updateCountryDataJobForm.controls.endDate.value;
    const startDate = moment(sd, 'MM/DD/YYYY').format('YYYY-MM-DD');
    const endDate = moment(ed, 'MM/DD/YYYY').format('YYYY-MM-DD');
    this.adminService.startUpdateCountryDataJob(startDate, endDate)
    .subscribe(
    (response) => {
      this.response = response.message;
      this.loading = false;
      const sub = interval(3000).pipe(
        switchMap(() => this.batchJobStatusService.getJobStatus('updateCountryData')),
      ).subscribe(response => {
        this.updateCountryDataJobStatus = response.status;
        console.log(status);
        if (response.status === 'COMPLETED' || response.status ==='FAILED') {
          sub.unsubscribe();
          if (response.status === 'FAILED') {
            this.errorMessage = 'updateCountryData job failed.'
          }
          this.disableButtons = false;
        }
      });
    },
    (error) => {
      this.errorMessage = error.message;
      this.response = null;
      this.loading = false;
      this.disableButtons = false;
    });
  }

  startUpdateCountryDailyDataJob() {
    this.errorMessage = null;
    this.loading = true;
    this.disableButtons = true;
    this.adminService.startUpdateCountryDailyDataJob().subscribe(
      (response) => {
        this.response = response.message;
        this.loading = false;
        const sub = interval(3000).pipe(
          switchMap(() => this.batchJobStatusService.getJobStatus('updateDailyCountryData')),
        ).subscribe(response => {
          this.updateCountryDataJobStatus = response.status;
          console.log(status);
          if (response.status === 'COMPLETED' || response.status ==='FAILED') {
            sub.unsubscribe();
            if (response.status === 'FAILED') {
              this.errorMessage = 'updateDailyCountryData job failed.'
            }
            this.disableButtons = false;
          }
        });
      },
      (error) => {
        this.errorMessage = error.message;
        this.response = null;
        this.loading = false;
        this.disableButtons = false;
      });
  }

  hide() {
    this.showAdmin = false;
  }

}
