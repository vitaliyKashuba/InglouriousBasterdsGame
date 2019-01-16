import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HttpRequesterService {

  // url = 'http://10.109.33.50:5000/';
  url = environment.backendUrl;

  constructor(private http: HttpClient) { }

  joinRequest(roomId: number, playerName: string) {
    const body = {name: playerName};
    return this.http.post(this.url + 'join/' + roomId, body);
  }

  sendCharacter(playerId: number, character: string) {
    const body = {id: playerId, character: character};
    return this.http.post(this.url + 'setCharacter', body);
  }
}
