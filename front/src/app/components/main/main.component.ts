import { Component, OnInit } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  title = 'app';

  greetings: string[] = [];
  showConversation = false;
  ws: any;
  name: string;
  disabled: boolean;

  personalWebSocket: any;
  broadcastWebSocket: any;

  private serverUrl = 'http://localhost:5000/socket';

  constructor() {}

  ngOnInit() {
  }

  connect() {

    // const ws = new SockJS(this.serverUrl);
    // this.stompClient = Stomp.over(ws);

    // connect to stomp where stomp endpoint is exposed
    const ws = new SockJS(this.serverUrl);
    // const socket = new WebSocket('ws://localhost:5000/socket');
    this.ws = Stomp.over(ws);
    const that = this;
    this.ws.connect({}, function(frame) {
      that.ws.subscribe('/errors', function(message) {
        alert('Error ' + message.body);
      });
      that.ws.subscribe('/topic/reply/' + 5, function(message) {
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

  sendName() {
    const data = JSON.stringify({
      'name' : this.name
    });
    console.log('send name: ' + name + ' ' + data);
    this.ws.send('/app/message/' + 5, {}, data);
  }

  sendPrivate() {
    const data = JSON.stringify({
      'name' : this.name
    });
    console.log('send name: ' + name + ' ' + data);
    this.ws.send('/app/private_message', {}, data);
  }

  showGreeting(message) {
    this.showConversation = true;
    this.greetings.push(message);
  }

  setConnected(connected) {
    this.disabled = connected;
    this.showConversation = connected;
    this.greetings = [];
  }

}
