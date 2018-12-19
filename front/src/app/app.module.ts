import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

import { MzInputModule } from 'ngx-materialize';
import { MzButtonModule } from 'ngx-materialize';
import { MzTooltipModule } from 'ngx-materialize';
import { MzIconModule, MzIconMdiModule } from 'ngx-materialize';

import { AppComponent } from './app.component';
import { MainComponent } from './components/main/main.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { ChatComponent } from './components/chat/chat.component';
import { CharactersComponent } from './components/characters/characters.component';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    WelcomeComponent,
    ChatComponent,
    CharactersComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MzInputModule,
    MzButtonModule,
    MzTooltipModule,
    MzIconModule,
    MzIconMdiModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
