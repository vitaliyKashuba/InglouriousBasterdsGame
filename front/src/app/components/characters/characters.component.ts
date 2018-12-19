import {Component, Input, OnInit} from '@angular/core';
import {log} from 'util';
import {HttpRequesterService} from '../../services/http-requester.service';
import {Teammate} from '../../model/teammate';
import {JqueryUtilService} from '../../services/jquery-util.service';

@Component({
  selector: 'app-characters',
  templateUrl: './characters.component.html',
  styleUrls: ['./characters.component.css']
})
export class CharactersComponent implements OnInit {

  @Input() playerId: number;
  @Input() teammates: Teammate[];

  character: string;

  sendFormId = 'sendFormId';
  // teammatesListId = 'teammatesListId';

  constructor(private requester: HttpRequesterService,
              private jq: JqueryUtilService) { }

  ngOnInit() {
    this.teammates.push(new Teammate('name', 'character'));
    this.teammates.push(new Teammate('qwerty', '12345r'));
  }

  onSendCharacterClick() {
    log(this.character);
    this.requester.sendCharacter(this.playerId, this.character).subscribe(
      data => this.handleSuccess(data),
      error => this.handleError(error)
    );
  }

  handleSuccess(data: any) {
    this.jq.scaleOut(this.sendFormId);
    // this.jq.scaleIn(this.teammatesListId);
    log(data);
  }

  handleError(error: any) {
    log(error);
  }

}
