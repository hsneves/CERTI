import { Injectable, NgZone } from "@angular/core";
import { NgEventBus } from "ng-event-bus";
import { Observable } from "rxjs";
import { Constants } from "../utils/constants";

@Injectable({
  providedIn: 'root'
})
export class PokemonCaughtSseService {

  private RECONNECTION_TIMEOUT = 5000;
  private clientId!: number | null;
  private url = Constants.SSE_POKEMONS_CAUGHT_CONNECT;
  private evtSource!: EventSource | null;

  constructor(private eventBus: NgEventBus, private zone: NgZone) { }

  /**
   * Retorna o clientId associado a conexão ao servidor SSE
   * 
   * @returns 
   */
  getClientId(): number {
    this.clientId = this.generateClientId();
    return this.clientId;
  }

  /**
   * Gera um clientId aleatório para o servidor armazenar as informações para envio das notificações push
   * 
   * @returns 
   */
  generateClientId(): number {
    return Math.floor(Math.random() * (9999999 - 1 + 1)) + 1;
  }

  /**
   * Conecta ao serviço SSE para receber as notificações de pokemons capturados
   * 
   * @returns 
   */
  connect(): Observable<string> {

    return new Observable<string>(observer => {

      console.log("PokemonCaughtSseService.connect() | Conectando ao SSE para receber notificações do servidor, uri = [" + this.url + "]");

      this.evtSource = new EventSource(this.url + this.getClientId());

      this.evtSource.addEventListener('message', (evt) => {
        console.log("PokemonCaughtSseService.connect() | Evento SSE recebido, evento = [" + evt.data + "]");
        this.zone.run(() => {
          observer.next(evt.data);
        });
      });

      this.evtSource.onopen = (ev) => {
        console.log("PokemonCaughtSseService.connect() | Conexão iniciada com o SSE, adicionando mensagem ao barramento de conexão estabelecida. " + ev);
        this.zone.run(() => {
          this.eventBus.cast(Constants.SSE_CONNECTION_BUS, { error: false });
        });
      },

        // dispara para o barramento se acontecer algum erro
        this.evtSource.onerror = (error: any) => {
          console.log("PokemonCaughtSseService.connect() | Erro na conexão com o event source, adicionando mensagem ao barramento de servidor indisponível.");
          this.reconnectOnError();
          this.zone.run(() => {
            this.eventBus.cast(Constants.SSE_CONNECTION_BUS, { error: true, message: "Servidor indisponível" });
          });
        };
    });
  }

  /**
   * Tenta reconectar após 5 segundos
   */
  private reconnectOnError(): void {

    // descarta a conexão atual
    this.dispose();

    console.log("PokemonCaughtSseService.reconnectOnError() | Tentando reconectar ao SSE em [" + this.RECONNECTION_TIMEOUT + "ms]");

    // atribui o timeout para a tentativa de reconexâo
    const self = this;

    setTimeout(() => {
      console.log("PokemonCaughtSseService.reconnectOnError() | Tentando reconectar ao SSE...");
      self.connect().subscribe(json => {
        console.log("PokemonCaughtSseService.reconnectOnError() | conectado!");
        // Invoca o EventBus para que seja propagado para os ouvintes
        this.zone.run(() => {
          this.eventBus.cast(Constants.SSE_CONNECTION_BUS, { error: false });
        });
      });;
    }, this.RECONNECTION_TIMEOUT);
  }

  /**
   * Descarta a conexão SSE com o servidor
   */
  dispose(): void {

    if (this.evtSource != null) {
      console.log("PokemonCaughtSseService.dispose() | Descartando conexão SSE, clientId = [" + this.clientId + "]");
      this.evtSource.close();
      this.evtSource = null;
    } else {
      console.log("PokemonCaughtSseService.dispose() | Event source é nulo, descartando..");
    }
  }
}
