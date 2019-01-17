import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

import { MzInputModule } from 'ngx-materialize';
import { MzButtonModule } from 'ngx-materialize';
import { MzTooltipModule } from 'ngx-materialize';
import { MzIconModule, MzIconMdiModule } from 'ngx-materialize';
import { MzToastModule } from 'ngx-materialize';
import { MzSpinnerModule } from 'ngx-materialize';

import { AppComponent } from './app.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { DesktopViewComponent } from './components/desktop-view/desktop-view.component';
import { MobileViewComponent } from './components/mobile-view/mobile-view.component';
import { IbCharacterInputComponent } from './components/ib-character-input/ib-character-input.component';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    DesktopViewComponent,
    MobileViewComponent,
    IbCharacterInputComponent
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
    MzIconMdiModule,
    MzToastModule,
    MzSpinnerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
