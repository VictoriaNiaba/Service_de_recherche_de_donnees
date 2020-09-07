import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ParkingRoutingModule } from './parking-routing.module';
import { ParkingComponent } from './parking.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [ParkingComponent],
  imports: [
    CommonModule,
    ParkingRoutingModule,
    SharedModule
  ]
})
export class ParkingModule { }
