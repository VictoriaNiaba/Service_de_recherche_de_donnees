import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Measure } from '../core/model/measure/measure';

@Injectable({
  providedIn: 'root'
})
export class ParkingService {

  constructor(private http: HttpClient) { }

  getMeasures(id?: string): Observable<Measure[]> {
    let query = "http://localhost:8080/web-of-things/parking/measures";

    if (id) {
      query += "?id=" + id;
    }

    return this.http.get<Measure[]>(query);
  }
}
