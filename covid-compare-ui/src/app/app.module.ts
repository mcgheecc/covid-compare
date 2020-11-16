import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';

import * as echarts from 'echarts';
import {NgxEchartsModule} from 'ngx-echarts';

import {HttpClientModule} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {ClarityModule} from '@clr/angular';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgSelectModule} from '@ng-select/ng-select';
import { CasesChartComponent } from './component/cases-chart/cases-chart.component';
import { DeathChartComponent } from './component/death-chart/death-chart.component';
import { CountryChartDataService } from './service/country-chart-data.service';
import { CountryService } from './service/country.service';
import { HeaderComponent } from './component/header/header.component';
import { FooterComponent } from './component/footer/footer.component';
import { SearchComponent } from './component/search/search.component';
import { AdminComponent } from './component/admin/admin.component';

@NgModule({
  declarations: [
    AppComponent,
    CasesChartComponent,
    DeathChartComponent,
    HeaderComponent,
    FooterComponent,
    SearchComponent,
    AdminComponent
  ],
  imports: [
    BrowserModule,
    NgxEchartsModule.forRoot({
      echarts
    }),
    HttpClientModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    ClarityModule,
    BrowserAnimationsModule,
    NgSelectModule
  ],
  providers: [
    CountryChartDataService,
    CountryService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
