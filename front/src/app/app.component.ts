import { Component } from '@angular/core';
import {log} from 'util';
import {Teammate} from './model/teammate';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  room: number;
  playerId: number;

  teammates: Teammate[];

  setRoomId(room: number) {
    this.room = room;
  }

  setPlayerId(id: number) {
    this.playerId = id;
  }

  setTeammates(data: Teammate[]) {
    this.teammates = data;
    log('setted players!');
    log(this.teammates);
  }
}
