import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {log} from 'util';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  @ViewChild('enterButton') enterButton: ElementRef;

  name: string;

  constructor() { }

  ngOnInit() {
  }

  onInputKeyUp() {
    log(this.name);
    if (this.name.length < 4) {
      this.scaleIn(this.enterButton);
    } else {
      this.scaleOut(this.enterButton);
    }
  }

  scaleIn(element: ElementRef) {
    element.nativeElement.classList.remove('scale-in');
    element.nativeElement.classList.add('scale-out');
  }

  scaleOut(element: ElementRef) {
    element.nativeElement.classList.remove('scale-out');
    element.nativeElement.classList.add('scale-in');
  }

}
