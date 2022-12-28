import 'dart:async';
import 'dart:convert';
import 'dart:math';

// ignore: depend_on_referenced_packages
import 'package:certi_cta_flutter/events/pokemon_caught_event.dart';
import 'package:certi_cta_flutter/events/sse_connection_event.dart';
import 'package:certi_cta_flutter/main.dart';
import 'package:certi_cta_flutter/model/pokeball.dart';
import 'package:certi_cta_flutter/utils/constants.dart';
// ignore: depend_on_referenced_packages
import 'package:logger/logger.dart';
// ignore: depend_on_referenced_packages
import 'package:universal_html/html.dart' as html;

class PokemonCaughtSse {
  final Logger logger = Logger();

  // late Sse sse;
  late html.EventSource eventSource;
  late Stream stream;

  late StreamController<String> streamController;

  int sseStatus = sseConnecting;

  void connect() {
    int clientId = Random().nextInt(500000);
    String uri = "$urlPokemonCaughtSse$clientId";

    //final streamController = StreamController<String>();
    eventSource = html.EventSource(uri.toString());
    streamController = StreamController<String>();

    // adiciona o stream controller
    eventSource.addEventListener('message', (html.Event message) {
      streamController.add((message as html.MessageEvent).data as String);
    });

    /// fechar se o endpoint não estiver funcionando e tentar a reconexão
    eventSource.onError.listen((event) {
      logger.e(
          "Cliente SSE desconectado, fazendo nova tentativa em 5 segundos...");
      // dispara o evento para o barramento
      sseStatus = sseDisconnected;
      eventBus.fire(SseConnectionEvent(false));
      reconnectOnError();
    });

    stream = streamController.stream;

    // Eventos recebidos pelo SSE
    stream.listen((event) {
      // listener SSE que recebe as notificações de pokemons capturados
      String result = event.toString();
      logger.d("Resultado da captura = [$result]");

      var message = json.decode(result);

      logger.d("Tipo da mensagem = [${message['type']}]");
      logger.d("Dados! ${message['data']}");

      switch (message['type']) {
        case sseMessageTypeConnected:
          sseStatus = sseConnected;
          // dispara para o barramento que ocorreu a conexão
          eventBus.fire(SseConnectionEvent(true));
          break;
        case sseMessageTypePokemonCaught:
          logger.w("Pokebola retornou! ${message['data']}");
          Pokeball pokeball = Pokeball.fromJson(message['data']);
          // dispara para o barramento que uma pokebola foi retornada
          eventBus.fire(PokemonCaughtEvent(pokeball));
          break;
      }
    });
  }

  void reconnectOnError() {
    Timer(const Duration(seconds: 5), () {
      logger.e("Tentando reconectar ao servidor SSE...");
      dispose();
      connect();
    });
  }

  void dispose() {
    eventSource.close();
    streamController.close();
  }
}
