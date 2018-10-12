import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpRequesterService {

  url = 'http://10.109.33.50:5000/';

  constructor(private http: HttpClient) { }

  joinRequest(roomId: number, playerName: string) {
    const body ={name: playerName};
    return this.http.post(this.url + 'join/' + roomId, body);
  }
}
