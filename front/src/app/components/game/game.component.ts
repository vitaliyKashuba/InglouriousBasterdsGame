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
    this.collection = [];
    log(this.gameData);
    switch (this.gameType) {
      case 0: // ib
        const data = JSON.parse(this.gameData);
        data.forEach(teammate => { this.collection.push(teammate.name + ' - ' + teammate.character); });
        this.header = 'Teammates';
        break;
      case 1: // spyfall
        break;
      case 2: // mafia
        break;
      default:
        log('smth going wrong, unknown game tytpe' + this.gameType);
    }

    // this.requester.getRandomSpyfallLocations().subscribe(
    //   data => this.collection = data as string[],
    //   error => log(error)
    // );
  }

}
