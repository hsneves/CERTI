import { Component, OnInit, OnDestroy, HostListener, NgZone } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MetaData, NgEventBus } from 'ng-event-bus';
import { Pokeball } from 'src/app/models/pokeball';
import { PokemonService } from 'src/app/services/pokemon.service';
import { Constants } from 'src/app/utils/constants';

@Component({
  selector: 'app-pokeballs',
  templateUrl: './pokeballs.component.html',
  styleUrls: ['./pokeballs.component.css']
})
export class PokeballsComponent implements OnInit, OnDestroy {

  pokeballs: Array<Pokeball> = new Array();

  pokeballsLoaded: boolean = false;

  pokeball!: Pokeball | null;

  errorMessage!: String;

  pokemonFormGroup: FormGroup;

  constructor(private pokemonService: PokemonService, private eventBus: NgEventBus, private formBuilder: FormBuilder, private zone: NgZone) {
    this.pokemonFormGroup = formBuilder.group({
      pokemonName: ["", Validators.required,]
    });
  }

  /**
   * Ao inicializar a aplicação
   */
  ngOnInit(): void {

    console.log("PokeballsComponent.ngOnInit() | Inicializando componente PokeballsComponent...");

    // recupera os pokemons capturados e popula a lista de pokebolas no componente
    this.loadPokeballs();

    // adiciona ouvinte ao event bus para quando uma nova notificação SSE de pokemon capturado chegar
    this.eventBus.on(Constants.SSE_MESSAGE_EVENT_BUS).subscribe((meta: MetaData) => {
      console.log("PokeballsComponent.ngOnInit() | Mensagem SSE recebida pelo Event Bus! " + meta.data);
      this.zone.run(() => {
        this.handleSseMessage(JSON.parse(meta.data));
      });
    });

    // adiciona ouvinte ao event bus para quando ocorrer uma exceção nas requisições
    this.eventBus.on(Constants.HTTP_INTERCEPTOR_REST).subscribe((meta: MetaData) => {
      console.log("PokeballsComponent.ngOnInit() | Erro na conexão.. " + meta.data);
      this.zone.run(() => {
        this.handleSseConnection(meta.data);
      });
    });

    // adiciona o ouvinte ao event bus para quando ocorrer uma exceção no SSE
    this.eventBus.on(Constants.SSE_CONNECTION_BUS).subscribe((meta: MetaData) => {
      console.log("PokeballsComponent.ngOnInit() | Evento de estado de conexão do SSE " + meta.data);
      this.zone.run(() => {
        this.handleSseConnection(meta.data);
      });
    });

    // inicia serviço SSE para notificações de novos pokemons capturados
    this.pokemonService.connectPokemonCaughtSse();
  }

  /**
   * Ao finalizar a aplicação
   */
  @HostListener('window:beforeunload')
  ngOnDestroy(): void {

    console.log("PokeballsComponent.ngOnDestroy() | Destruindo componente PokeballsComponent...");

    // descarta o serviço SSE
    this.pokemonService.disposePokemonCaughtSse();
  }

  /**
   * 
   * @param data 
   */
  private handleSseMessage(data: any) {

    console.log("PokeballsComponent.handleSseMessage() | Tratamento da mensagem SSE = [" + data + "]");

    // se for mensagem de conectado
    switch (data.type) {
      case Constants.ServerSentEventsMessageType.CONNECTED:
        this.zone.run(() => {
          // limpa as mensagens de erro
          this.clearErrorMessage();
          // recarregar a tabela de pokemons
          this.loadPokeballs();
        });
        break;
      case Constants.ServerSentEventsMessageType.POKEMON_CAUGHT:
        this.addNewPokeball(data.data);
        break;
    }

  }

  /**
   * Atribui a mensagem de erro ao componente para ser apresentada na tela.
   */
  private handleSseConnection(result: any): void {

    if (result.error) {
      console.log("PokeballsComponent.handleSseConnection() | Erro na requisição HTTP = [" + result.message + "]");
      // limpar a lista de pokebolas capturadas
      this.setErrorMessage(result.message);
      this.removeSelfData();
    } else {
      console.log("PokeballsComponent.handleSseConnection() | Conexão bem sucedida, limpando registros e atualizando pokebolas");
      this.clearAndRefreshData();
    }
  }

  /**
   * Atribui a mensagem de erro ao componente
   * 
   * @param errorMessage mensagem de erro
   */
  private setErrorMessage(errorMessage: String): void {
    this.errorMessage = errorMessage;
  }

  /**
   * Limpa os dados do compoente limpando as variáveis
   */
  private removeSelfData(): void {
    this.pokeballs = [];
    this.pokeball = null;
    this.pokeballsLoaded = false;
  }

  /**
   * Limpa as mensagens de erro e recarrega as pokebolas
   */
  private clearAndRefreshData(): void {
    this.clearErrorMessage();
    this.removeSelfData();
    this.loadPokeballs();
  }

  /**
   * Limpa a mensagem de erro para não ser mais apresentada na tela
   */
  private clearErrorMessage(): void {
    this.errorMessage = "";
  }


  /**
   * Adicionar a pokebola (se existir pokemon) na mochila
   * 
   * @param pokeball 
   */
  private addNewPokeball(pokeball: Pokeball): void {
    console.log("PokeballsComponent.addNewPokeball() | Pokebola  = [" + JSON.stringify(pokeball) + "]");
    if (pokeball.pokemon != null) {
      console.log("PokeballsComponent.addNewPokeball() | Adicionando Pokemon " + pokeball.pokemon.name + " na mochila!");
      // Adicionar nova pokebola no início do array (somente se existir um pokemon dentro)
      this.pokeballs = [pokeball].concat(this.pokeballs);
    } else {
      console.log("PokeballsComponent.addNewPokeball() | Pokebola vazia, tente novamente!");
    }
  }

  /**
   * Lança uma Pokebola para capturar um Pokemon
   */
  throwPokeball(): void {
    console.log("PokeballsComponent.throwPokeball() | Pokebola Vaiiii! Tentando capturar Pokemon " + this.pokemonFormGroup.value.pokemonName);
    this.pokemonService.throwPokeball(this.pokemonFormGroup.value.pokemonName).subscribe(pokeball => {
      console.log("PokeballsComponent.throwPokeball() | Pokebola retornou! " + JSON.stringify(pokeball));
      this.pokeball = pokeball;
      if (pokeball.pokemon != null) {
        console.log("PokeballsComponent.throwPokeball() | Pokemon " + pokeball.pokemon.id + "/" + pokeball.pokemon.name + " capturado!");
      } else {
        console.log("PokeballsComponent.throwPokeball() | Pokemon " + this.pokemonFormGroup.value.pokemonName + " escapou!");
      }
      // cria um timer para remover a pokebola da mão
      setTimeout(() => {
        console.log("PokeballsComponent.throwPokeball() | Guardando a pokebola na mochila...");
        this.pokeball = null;
      }, 5000);
    });
  }

  /**
   * Recupera as pokebolas na API REST!
   */
  private loadPokeballs() {
    console.log("PokeballsComponent.loadPokeballs() | Carregando pokebolas...");
    if (!this.pokeballsLoaded) {
      this.pokemonService.getPokeballs().subscribe(pokeballs => {
        console.log("PokeballsComponent.loadPokeballs() | Pokebolas carregadas = [" + pokeballs.length + "]");
        this.pokeballs = pokeballs;
        this.pokeballsLoaded = true;
      });
    } else {
      console.log("PokeballsComponent.loadPokeballs() | Pokebolas já carregadas = [" + this.pokeballs.length + "]");
    }
  }

  /**
   * Retorna a imagem do pokemon, caso a pokebola não capture o pokemon deve voltar a imagem da pokebola vazia
   * 
   * @param pokemon 
   */
  getPokemonImageUrl(pokeball: Pokeball): string {
    if (pokeball.pokemon != null) {
      return Constants.URL_POKEMON_IMAGE + pokeball.pokemon.id + ".png";
    } else {
      return Constants.URL_EMPTY_POKEBALL;
    }
  }

}
