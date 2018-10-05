import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MzInputModule } from 'ngx-materialize';
import { MzButtonModule } from 'ngx-materialize';

import { AppComponent } from './app.component';
import { MainComponent } from './components/main/main.component';
import { WelcomeComponent } from './components/welcome/welcome.component';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    WelcomeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    BrowserAnimationsModule,
    MzInputModule,
    MzButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
