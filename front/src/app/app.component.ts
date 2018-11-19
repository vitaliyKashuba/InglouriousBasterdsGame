import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  room: number;
  playerId: number;

  setRoomId(room: number) {
    this.room = room;
  }

  setPlayerId(id: number) {
    this.playerId = id;
  }
}
