import { Component, OnInit } from '@angular/core';
import {log} from 'util';
import {HttpRequesterService} from '../../services/http-requester.service';

@Component({
  selector: 'app-characters',
  templateUrl: './characters.component.html',
  styleUrls: ['./characters.component.css']
})
export class CharactersComponent implements OnInit {

  character: string;

  constructor(private requester: HttpRequesterService) { }

  ngOnInit() {
  }

  onSendCharacterClick() {
    log(this.character);
  }

}
