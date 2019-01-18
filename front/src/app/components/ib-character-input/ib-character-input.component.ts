import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {log} from 'util';
import {MzToastService} from 'ngx-materialize';
import {HttpRequesterService} from '../../services/http-requester.service';

@Component({
  selector: 'app-ib-character-input',
  templateUrl: './ib-character-input.component.html',
  styleUrls: ['./ib-character-input.component.css']
})
export class IbCharacterInputComponent implements OnInit {

  @Input() playerId: number;
  @Output() onCharacterSet = new EventEmitter<any>();
  character: string;

  constructor(private toastService: MzToastService,
              private requester: HttpRequesterService) { }

  ngOnInit() {
  }

  sendCharacter() {
    if (!this.character) {
      this.toastService.show('enter character', 4000, 'red');
    } else {
      this.requester.sendCharacter(this.playerId, this.character)
        .subscribe(success => this.handleSuccess(), error => this.handleError(error));
    }
  }

  handleSuccess() {
    this.onCharacterSet.emit(null);
  }

  handleError(error) {
    log(error);
  }

}
