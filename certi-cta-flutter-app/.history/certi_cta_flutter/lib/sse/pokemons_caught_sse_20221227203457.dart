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

  final streamController = StreamController<String>();

  void connect2() {
    int clientId = Random().nextInt(500000);
    String uri = "$urlPokemonCaughtSse$clientId";

    //final streamController = StreamController<String>();
    eventSource = html.EventSource(uri.toString());

    // adiciona o stream controller
    eventSource.addEventListener('message', (html.Event message) {
      streamController.add((message as html.MessageEvent).data as String);
    });

    /// fechar se o endpoint não estiver funcionando e tentar a reconexão
    eventSource.onError.listen((event) {
      logger.e(
          "Cliente SSE desconectado, fazendo nova tentativa em 5 segundos...");
      eventSource.close();
      streamController.close();
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
    }, onError: (event) {
      logger.e("SSE desconectado, tentando reconectar em 5 segundos");
      // eventBus.fire(SseConnectionEvent(false));
    });
  }

  Stream get stream => streamController.stream;

  void reconnectOnError() {
    Timer(const Duration(seconds: 5), () {
      logger.e("Tentando reconectar ao servidor SSE...");
      connect2();
    });
  }

  void dispose2() {
    eventSource.close();
    streamController.close();
  }

  /*connect() {
    int clientId = Random().nextInt(500000);
    String uri = "$urlPokemonCaughtSse$clientId";

    logger.i(
        "Conectando ao SSE para receber notificações do servidor, uri = [$uri]");

    sse = Sse.connect(uri: Uri.parse(uri));

    stream = sse.stream;
    stream.listen((event) {
      // listener SSE que recebe as notificações de pokemons capturados
      String result = event.toString();
      logger.d("Resultado da captura = [$result]");

      var message = json.decode(result);

      logger.d("Tipo da mensagem = [${message['type']}]");
      logger.d("Dados! ${message['data']}");

      switch (message['type']) {
        case sseMessageTypeConnected:
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
    }, onError: (event) {
      logger.e("SSE desconectado, tentando reconectar em 5 segundos");
      // eventBus.fire(SseConnectionEvent(false));
    });
  }

  dispose() {
    sse.close();
  }
}

class Sse {
  final html.EventSource eventSource;
  final StreamController<String> streamController;

  Sse._internal(this.eventSource, this.streamController);

  factory Sse.connect({required Uri uri}) {
    final streamController = StreamController<String>();
    final eventSource = html.EventSource(uri.toString());

    eventSource.addEventListener('message', (html.Event message) {
      streamController.add((message as html.MessageEvent).data as String);
    });

    /// fechar se o endpoint não estiver funcionando
    eventSource.onError.listen((event) {
      eventSource.close();
      streamController.close();
      eventBus.fire(SseConnectionEvent(false));
    });

    return Sse._internal(eventSource, streamController);
  }

  Stream get stream => streamController.stream;

  bool isClosed() => streamController.isClosed;

  void close() {
    eventSource.close();
    streamController.close();
  }*/
}
