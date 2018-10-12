import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {log} from 'util';
import {JqueryUtilService} from '../../services/jquery-util.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  name: string;
  nameEntered: boolean;
  nameInputButtonId = 'enterButton';
  startButtonsRowId = 'sbRowId';
  enterRoomId = 'enterRoomId';

  constructor(private jq: JqueryUtilService) { }

  ngOnInit() {
  }

  onInputKeyUp() {
    this.nameEntered = false;
    if (this.name.length < 3) {
      this.jq.scaleOut(this.nameInputButtonId);
    } else {
      this.jq.scaleIn(this.nameInputButtonId);
    }
  }

  onNameInputButtonClick() {
    this.nameEntered = true;
    this.jq.scaleOut(this.nameInputButtonId);
    this.jq.scaleIn(this.startButtonsRowId);
  }

  onJoinClick() {
    this.jq.scaleIn(this.enterRoomId);
  }

}
