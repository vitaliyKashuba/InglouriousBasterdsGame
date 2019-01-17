import { Component } from '@angular/core';
import {log} from 'util';
import {Teammate} from './model/teammate';
import device from 'current-device';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isMobile = device.mobile();

  showCharacterInput: boolean;

  roomId: number;
  playerId: number;
  game: number; //  0 for i
  teammates: Teammate[];

  ws: any;
  disabled: boolean;
  serverUrl = environment.backendUrl + 'socket';

  setRoomId(room: number) {
    this.roomId = room;
    this.connect();
  }

  playerJoined(data: any) {
    this.playerId = data.id;
    this.game = data.game;
    this.showCharacterInput = this.game === 0;
  }

  setTeammates(data: Teammate[]) {
    this.teammates = data;
    log('setted players!');
    log(this.teammates);
  }

  connect() {
    log('connect');
    const ws = new SockJS(this.serverUrl);

    this.ws = Stomp.over(ws);
    const that = this;
    this.ws.connect({}, function(frame) {
      that.ws.subscribe('/errors', function(message) {
        alert('Error ' + message.body);
      });
      that.ws.subscribe('/topic/reply/' + that.roomId, function(message) {
        // console.log('broadcast' + message);
        // that.showGreeting(message.body);
      });
      that.ws.subscribe('/user/topic/reply', function(message) {
        if (message.body.startsWith('teammates')) {
          that.setPlayers(message.body.substring(9));
        }
        // console.log('private' + message);
        // that.showGreeting(message.body);
      });
      that.disabled = true;

      that.sendPrivate('setPrincipal:' + that.playerId);

    }, function(error) {
      alert('STOMP error ' + error);
    });
  }

  disconnect() {
    if (this.ws != null) {
      this.ws.ws.close();
    }
    this.setConnected(false);
    console.log('Disconnected');
  }

  setConnected(connected) {
    this.disabled = connected;
    // this.showConversation = connected;
    // this.greetings = [];
  }

  sendBroadcast(message: string) {
    const data = JSON.stringify({
      'name' : message
    });
    // console.log('send: ' + data);
    this.ws.send('/app/message/' + this.roomId, {}, data);
  }

  sendPrivate(message: string) {
    // const data = JSON.stringify(message);
    // console.log('send name: ' + data);
    this.ws.send('/app/private_message', {}, message);
  }

  setPlayers(players: string) {
    log('setting players');
    const m = new Map<string, string>();
    m.set('wer', 'rwrew');
    const data = JSON.parse(players);

    // log(m);
    log(data.constructor.name);

    let shit = Object.entries(data);

    log(shit.keys());

    // data.forEach((value: string, key: string) => {
    //   console.log(key, value);
    // });
    // this.setTeammatesEmitter.emit(data as Teammate[]);
  }
}
