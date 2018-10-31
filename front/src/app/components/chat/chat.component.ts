import {Component, Input, OnInit} from '@angular/core';
import {WebSocketService} from '../../services/web-socket.service';
import {log} from 'util';
import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  @Input() roomId: number;

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
        console.log('broadcast' + message);
        that.showGreeting(message.body);
      });
      that.ws.subscribe('/user/topic/reply', function(message) {
        console.log('private' + message);
        that.showGreeting(message.body);
      });
      that.disabled = true;
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

  showGreeting(message) {
    // this.showConversation = true;
    // this.greetings.push(message);
    log('resieved ' + message);
  }

  sendBroadcast(message: string) {
    const data = JSON.stringify({
      'name' : message
    });
    console.log('send: ' + data);
    this.ws.send('/app/message/' + this.roomId, {}, data);
  }

  sendPrivate(message: string) {
    const data = JSON.stringify({
      'name' : message
    });
    console.log('send name: ' + data);
    this.ws.send('/app/private_message', {}, data);
  }

}
