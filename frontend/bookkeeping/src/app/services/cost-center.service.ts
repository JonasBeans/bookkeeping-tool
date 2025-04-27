import {inject, Injectable, OnInit} from '@angular/core';
import {CostCenter} from "../../dto/cost-center";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CostCenterService {

  public costCenters: CostCenter[] = [];
  public client: HttpClient = inject(HttpClient);

  constructor() {
    this.client.get<CostCenter[]>('http://localhost:8080/api/cost-centers/all')
      .subscribe((response) =>  this.costCenters = response)
  }

}
