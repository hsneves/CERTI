import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormBuilder } from "@angular/forms";

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgEventBus } from 'ng-event-bus';

import { AppComponent } from './app.component';
import { PokeballsComponent } from './components/pokeballs/pokeballs.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PokemonService } from './services/pokemon.service';
import { PokemonCaughtSseService } from './sse/pokemon-caught-sse.service';

import { HttpErrorInterceptor } from './interceptors/http_error_interceptor';


@NgModule({
  declarations: [
    AppComponent,
    PokeballsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    HttpClientModule,
    NgEventBus,
    PokemonService,
    PokemonCaughtSseService,
    FormBuilder,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true
    },
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {

}

// serviço de barramento de mensagem/evento
export const eventBus = new NgEventBus();