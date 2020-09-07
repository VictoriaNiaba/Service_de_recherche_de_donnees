import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ParkingService } from './parking.service';
import { Measure } from '../core/model/measure/measure';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith, debounceTime } from 'rxjs/operators';

@Component({
  selector: 'app-parking',
  templateUrl: './parking.component.html',
  styleUrls: ['./parking.component.scss']
})
export class ParkingComponent implements AfterViewInit, OnInit {
  displayedColumns: string[] = ['time', 'id', 'value', 'unit'];
  dataSource = new MatTableDataSource<Measure>([]);
  @ViewChild(MatPaginator) paginator: MatPaginator;

  myControl = new FormControl();

  constructor(private parkingService: ParkingService) {}
  
  ngOnInit(): void {
    this._updateDataSource();
    this.myControl.valueChanges.pipe(debounceTime(1000)).subscribe(value => this._updateDataSource(value));
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  private _updateDataSource(value?: string) {
    this.parkingService.getMeasures(value).subscribe(measures => {
      this.dataSource.data = measures;
    });
  }
}