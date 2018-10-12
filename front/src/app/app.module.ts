import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MzIconModule, MzIconMdiModule } from 'ngx-materialize';

import { MzInputModule } from 'ngx-materialize';
import { MzButtonModule } from 'ngx-materialize';
import { MzTooltipModule } from 'ngx-materialize';

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
    MzButtonModule,
    MzTooltipModule,
    MzIconModule,
    MzIconMdiModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
