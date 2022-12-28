import 'dart:async';
import 'dart:convert';
import 'dart:math';

// ignore: depend_on_referenced_packages
import 'package:certi_cta_flutter/events/pokemon_caught_event.dart';
import 'package:certi_cta_flutter/main.dart';
import 'package:certi_cta_flutter/model/pokeball.dart';
import 'package:certi_cta_flutter/repositories/pokeball_repository.dart';
import 'package:certi_cta_flutter/services/pokemon_service.dart';
import 'package:certi_cta_flutter/utils/constants.dart';
import 'package:dio/dio.dart';

// ignore: depend_on_referenced_packages
import 'package:universal_html/html.dart' as html;

// ignore: depend_on_referenced_packages
import 'package:logger/logger.dart';

class PokemonCaughtSse {
  final Logger logger = Logger();

  final PokemonService pokemonService =
      PokemonService(PokeballRepository(), Dio());

  final int clientId = Random().nextInt(500000);
  late Sse sse;
  late Stream stream;

  connect() {
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

      switch (message.type) {
        case sseMessageTypeConnected:
          break;
        case sseMessageTypePokemonCaught:
          Pokeball pokeball = message['data'];
          logger.w("Pokebola retornou! ${pokeball.id}");
          eventBus.fire(PokemonCaughtEvent(pokeball));
          break;
      }
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
    });

    return Sse._internal(eventSource, streamController);
  }

  Stream get stream => streamController.stream;

  bool isClosed() => streamController.isClosed;

  void close() {
    eventSource.close();
    streamController.close();
  }
}
