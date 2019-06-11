import {Component, Input, OnInit} from '@angular/core';
import {HttpRequesterService} from '../../services/http-requester.service';
import {log} from 'util';
import {Teammate} from '../../model/teammate';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit {

  /**0 - IB, 1 - spyfall, 2 - mafia
   * because js enums sucks */
  @Input() gameType: number;

  /**server join response*/
  @Input() gameData: any;

  header: string;
  collection: string[];

  constructor(private requester: HttpRequesterService) { }

  ngOnInit() {
    let data;
    this.collection = [];
    switch (this.gameType) {
      case 0: // ib
        data = JSON.parse(this.gameData);
        data.forEach(teammate => { this.collection.push(teammate.name + ' - ' + teammate.character); });
        this.header = 'Teammates';
        break;
      case 1: // spyfall
        data = JSON.parse(this.gameData.locations);
        data.forEach(location => this.collection.push(location));
        this.header = this.gameData.role;
        break;
      case 2: // mafia
        this.header = this.gameData;
        break;
      default:
        log('smth going wrong, unknown game type' + this.gameType);
    }
  }

}
