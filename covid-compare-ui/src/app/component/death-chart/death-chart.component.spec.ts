import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeathChartComponent } from './death-chart.component';

describe('DeathChartComponent', () => {
  let component: DeathChartComponent;
  let fixture: ComponentFixture<DeathChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeathChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeathChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
