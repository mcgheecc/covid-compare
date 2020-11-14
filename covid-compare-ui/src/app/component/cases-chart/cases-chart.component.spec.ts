import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CasesChartComponent } from './cases-chart.component';

describe('CasesChartComponent', () => {
  let component: CasesChartComponent;
  let fixture: ComponentFixture<CasesChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CasesChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CasesChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
