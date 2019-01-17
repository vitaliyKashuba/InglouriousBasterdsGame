import {Component, ElementRef, Input, OnInit, Output, ViewChild, EventEmitter} from '@angular/core';
import {log} from 'util';
import {JqueryUtilService} from '../../services/jquery-util.service';
import {HttpRequesterService} from '../../services/http-requester.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  @Output() joinEmitter = new EventEmitter<any>();
  @Output() requestSentEmitter = new EventEmitter<any>();

  name: string;
  roomInput: number;

  nameEntered: boolean;

  nameInputButtonId = 'enterButton';
  startButtonsRowId = 'sbRowId';
  enterRoomId = 'enterRoomId';

  constructor(private jq: JqueryUtilService,
              private requester: HttpRequesterService) { }

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

  onMakeJoinRequestClick() {
    this.requestSentEmitter.emit(null);
    this.requester.joinRequest(this.roomInput, this.name).subscribe(
      data => this.successJoinHandler(data),
      error => this.errorHandler(error)
    );
  }

  successJoinHandler(data: any) {
    data.roomId = this.roomInput;
    this.joinEmitter.emit(data);
  }

  errorHandler(error: any) {
    log('error ' + error);
  }

}
