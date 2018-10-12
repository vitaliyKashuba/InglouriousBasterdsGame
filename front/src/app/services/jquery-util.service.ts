import { Injectable } from '@angular/core';
import {log} from 'util';

// declare var $: JQueryStatic;

@Injectable({
  providedIn: 'root'
})
export class JqueryUtilService {

  constructor() { }

  scaleOut(elementId: string) {
    $('#' + elementId).addClass('scale-out').removeClass('scale-in');
  }

  scaleIn(elementId: string) {
    log('scale out' + elementId);
    $('#' + elementId).addClass('scale-in').removeClass('scale-out');
  }
}
