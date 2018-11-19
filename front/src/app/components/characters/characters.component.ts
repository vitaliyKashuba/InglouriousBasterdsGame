import {Component, Input, OnInit} from '@angular/core';
import {log} from 'util';
import {HttpRequesterService} from '../../services/http-requester.service';
import {Teammate} from '../../model/teammate';

@Component({
  selector: 'app-characters',
  templateUrl: './characters.component.html',
  styleUrls: ['./characters.component.css']
})
export class CharactersComponent implements OnInit {

  @Input() playerId: number;
  @Input() teammates: Teammate[];

  character: string;

  constructor(private requester: HttpRequesterService) { }

  ngOnInit() {
  }

  onSendCharacterClick() {
    log(this.character);
    this.requester.sendCharacter(this.playerId, this.character).subscribe(
      data => this.handleSuccess(data),
      error => this.handleError(error)
    );
  }

  handleSuccess(data: any) {
    log(data);
  }

  handleError(error: any) {
    log(error);
  }

}
