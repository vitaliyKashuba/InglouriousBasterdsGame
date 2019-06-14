import {Component, ElementRef, Input, OnInit, Output, ViewChild, EventEmitter} from '@angular/core';
import {log} from 'util';
import {JqueryUtilService} from '../../services/jquery-util.service';
import {HttpRequesterService} from '../../services/http-requester.service';
import {MzToastService} from 'ngx-materialize';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  MIN_NAME_LENGTH = 3;

  @Output() joinEmitter = new EventEmitter<any>();
  @Output() requestSentEmitter = new EventEmitter<any>();

  name: string;
  roomInput: number;

  nameEntered: boolean;

  nameInputButtonId = 'enterButton';
  startButtonsRowId = 'sbRowId';
  enterRoomId = 'enterRoomId';

  constructor(private jq: JqueryUtilService,
              private requester: HttpRequesterService,
              private toastService: MzToastService) { }

  ngOnInit() {
    this.name = localStorage.getItem('name');
  }

  ngAfterViewInit() {
    if (this.name){
      this.jq.scaleIn(this.nameInputButtonId);
    }
  }

  onInputKeyUp() {
    this.nameEntered = false;
    if (this.name.length < this.MIN_NAME_LENGTH) {
      this.jq.scaleOut(this.nameInputButtonId);
    } else {
      this.jq.scaleIn(this.nameInputButtonId);
    }
  }

  onNameInputButtonClick() {
    this.nameEntered = true;
    localStorage.setItem('name', this.name);

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
    const message = error.status === 500 ? 'enter valid room number' : 'unknown error';
    this.toastService.show(message, 4000, 'red');
  }

}
