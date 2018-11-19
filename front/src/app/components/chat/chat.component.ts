import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {log} from 'util';
import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {Teammate} from '../../model/teammate';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  @Input() roomId: number;
  @Input() playerId: number;

  @Output() setTeammatesEmitter = new EventEmitter<any>();

  private serverUrl = 'http://localhost:5000/socket';

  // greetings: string[] = [];
  // showConversation = false;
  ws: any;
  // name: string;
  disabled: boolean;

  constructor() {
    log('constructor ' + this.roomId);
    this.connect();
  }

  ngOnInit(): void {
    log('init ' + this.roomId);
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
    this.setTeammatesEmitter.emit(data as Teammate[]);
  }

  showGreeting(message) {
    // this.showConversation = true;
    // this.greetings.push(message);
    log('resieved ' + message);
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

}
