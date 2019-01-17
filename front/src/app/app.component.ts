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

  /**boolean vals to show/hide html elements*/
  isShowWelcomeComponent = true;
  isShowCharacterInput: boolean;
  isShowGameComponent: boolean;
  isShowSpinner: boolean;
  loadingText: string;  /**text under spinner*/

  roomId: number;
  playerId: number;
  game: number; //  0 for ib
  gameData: any; // server response with ib players, spyfall locations or role. need to be parsed

  ws: any;
  // disabled: boolean;
  serverUrl = environment.backendUrl + 'socket';

  /**
   * calls when player sucessfully joined game from welcome component
   *
   * server response contains:
   *    player id (set by backend)
   *    game type (0 - ib, 1 - spyfall, 2 - mafia)
   *    room id
   */
  playerJoined(data: any) {
    this.hideSpinner();
    this.playerId = data.id;
    this.game = data.game;
    this.roomId = data.roomId;
    this.connect();

    this.isShowWelcomeComponent = false;
    if (this.game === 0) {
      this.isShowCharacterInput = true;
    } else {
      this.showSpinner('waiting till game start');
    }
  }

  /** calls when ib character successfully set from ib-character-input component*/
  ibCharacterSetHandler() {
    this.isShowCharacterInput = false;
    this.showSpinner('waiting till game start');
  }

  /**
   * make websocket connection
   * needs to start game from server init, unable to do it via http
   */
  connect() {
    log('connect');
    const ws = new SockJS(this.serverUrl);

    this.ws = Stomp.over(ws);
    const that = this;
    this.ws.connect({}, function(frame) {
      that.ws.subscribe('/errors', function(message) {
        that.handleWebsocketErrorMessage(message);
      });
      that.ws.subscribe('/topic/reply/' + that.roomId, function(message) {    // broadcast messages here (for chat)
        that.handleWebsocketBroadcastMessage(message);
      });
      that.ws.subscribe('/user/topic/reply', function(message) {        // user messages
        that.handleWebsocketDirectMessage(message);
      });
      // that.disabled = true;

      that.sendPrivate('setPrincipal:' + that.playerId);    // used to be able send direct messages to players
    }, function(error) {
      alert('STOMP error ' + error);
    });
  }

  handleWebsocketDirectMessage(message) {
    if (message.body.startsWith('teammates')) {
      this.startIbGame(message.body.substring(9));
      // const data = JSON.parse(message.body.substring(9));
      // this.setPlayers(message.body.substring(9));
    }             // TODO parse all message prefixes to start games
  }

  handleWebsocketBroadcastMessage(message) {
    log(message);
  }

  handleWebsocketErrorMessage(message) {
    log(message);
  }

  startIbGame(data: string) {
    this.gameData = data;
    this.isShowSpinner = false;
    this.isShowGameComponent = true;
  }

  // disconnect() {
  //   if (this.ws != null) {
  //     this.ws.ws.close();
  //   }
  //   this.setConnected(false);
  //   console.log('Disconnected');
  // }
  //
  // setConnected(connected) {
  //   this.disabled = connected;
  //   // this.showConversation = connected;
  //   // this.greetings = [];
  // }

  sendBroadcast(message: string) {
    const data = JSON.stringify({
      'name' : message
    });
    // console.log('send: ' + data);
    this.ws.send('/app/message/' + this.roomId, {}, data);
  }

  sendPrivate(message: string) {
    this.ws.send('/app/private_message', {}, message);
  }

  // setPlayers(players: string) {
  //   log('setting players');
  //   const m = new Map<string, string>();
  //   m.set('wer', 'rwrew');
  //   const data = JSON.parse(players);
  //
  //   log(data);
  //   // log(m);
  //   // log(data.constructor.name);
  //   //
  //   // let shit = Object.entries(data);
  //   //
  //   // log(shit.keys());
  //
  //   // data.forEach((value: string, key: string) => {
  //   //   console.log(key, value);
  //   // });
  //   // this.setTeammatesEmitter.emit(data as Teammate[]);
  // }

  showSpinner(text?: string) {
    this.loadingText = text ? text : '';
    this.isShowSpinner = true;
  }

  hideSpinner() {
    this.isShowSpinner = false;
  }
}
