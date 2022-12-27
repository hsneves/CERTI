import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Constants } from '../utils/constants';
import { PokemonCaughtSseService } from '../sse/pokemon-caught-sse.service';
import { NgEventBus } from 'ng-event-bus';
import { Pokeball } from '../models/pokeball';

@Injectable({
  providedIn: 'root'
})
export class PokemonService {

  constructor(private http: HttpClient, private eventBus : NgEventBus, private pokemonCaughtSseService: PokemonCaughtSseService) { }

  /**
   * Conecta ao serviço SSE para receber notificações de pokemons capturados
   */
  connectPokemonCaughtSse(): void {
    this.pokemonCaughtSseService.connect().subscribe(json => {
      console.log("Evento de captura de pokemon recebido! " + json);
      // Invoca o EventBus para que seja propagado para os ouvintes
      this.eventBus.cast(Constants.SSE_MESSAGE_EVENT_BUS, json);
    });
  }

  /**
   * Descarta a conexão SSE removendo
   */
  disposePokemonCaughtSse(): void {
    this.pokemonCaughtSseService.dispose();
    this.disposePokemonCaughtSseClientId();
  }

  /**
   * Desacopla o clientId no servidor SSE
   * 
   * @returns 
   */
  disposePokemonCaughtSseClientId(): void {
    var destroySseUrl = Constants.SSE_POKEMONS_CAUGHT_DESTROY + this.pokemonCaughtSseService.getClientId();
    console.log("Removendo recursos da conexão cliente no servidor, url = [" + destroySseUrl + "]");
    this.http.get(destroySseUrl).subscribe(result => {
      console.log("ClientId removido do servidor");
    });
  }

  /**
   * Consome a API REST para capturar um pokemon
   * 
   * @param pokemonName 
   * @returns 
   */
  throwPokeball(pokemonName: String): Observable<Pokeball> {
    var url = Constants.THROW_POKEBALL + pokemonName;
    console.log("Lançando Pokebola para capturar Pokemon " + pokemonName + ", url = [" + url + "]");
    return this.http.get<Pokeball>(url);
  }

  /**
   * Limpa a lista de pokemons capturados
   */
  clearPokeballs(): void {
    
  }

  /**
   * Consome a API REST para recuperar os pokemons capturados
   * 
   * @returns 
   */
  getPokeballs(): Observable<Pokeball[]> {
    console.log("Requisitando Pokebolas na API REST, url = [" + Constants.GET_POKEBALLS + "]");
    return this.http.get<Pokeball[]>(Constants.GET_POKEBALLS);
  }

}
