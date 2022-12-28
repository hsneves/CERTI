import 'dart:async';

import 'package:certi_cta_flutter/events/sse_connection_event.dart';
import 'package:certi_cta_flutter/main.dart';
import 'package:certi_cta_flutter/model/pokeball.dart';
import 'package:certi_cta_flutter/repositories/pokeball_repository.dart';
import 'package:certi_cta_flutter/sse/pokemons_caught_sse.dart';
import 'package:certi_cta_flutter/utils/constants.dart';
import 'package:dio/dio.dart';
// ignore: depend_on_referenced_packages
import 'package:logger/logger.dart';

import '../exceptions/pokemon_server_exception.dart';

class PokemonService {
  final Logger logger = Logger();

  final PokeballRepository repository;

  final Dio dio;

  final PokemonCaughtSse sse = PokemonCaughtSse();

  int sseStatus = sseConnecting;

  PokemonService(this.repository, this.dio) {
    // Evento de estado da conexão
    eventBus.on<SseConnectionEvent>().listen((event) {
      logger.i(
          "Evento de estado de conexão recebido pelo EventBus! conectado [${event.connected}}]");

      if (event.connected) {
        logger.i("SSE Conectado!");
        sseStatus = sseConnected;
      } else {
        logger.e("SSE Desconectado!");
        sseStatus = sseDisconnected;
      }
    });

    connectSse();
  }

  void connectSse() {
    // conecta ao sse
    sse.connect2();
  }

  void disposeSse() {
    sse.dispose2();
  }

  void handlePokeball(Pokeball pokeball) {
    if (pokeball.pokemon != null) {
      String? pokemon = pokeball.pokemon?.name;
      logger.i("Pokemon capturado! $pokemon");
      addPokemonCaught(pokeball);
    } else {
      logger.e("Pokemon escapou!");
    }
  }

  void addPokemonCaught(Pokeball pokeball) {
    logger.i("Adicionando novo pokemon capturado! ${pokeball.pokemon?.name}");
    repository.addPokeball(pokeball);
  }

  Future<Pokeball?> throwPokeball(String name) async {
    logger.i("Pokebola vai!!! Tentando capturar pokemon $name");

    Pokeball? pokeball;

    var data;

    try {
      String url = "$urlCatchPokemon$name";

      logger.d("Consumindo serviço REST na URL = [$url]");

      final response = await dio.get(url);

      var statusCode = response.statusCode;
      data = response.data;

      logger.d(
          "Response, statusCode = [$statusCode], data = [$data], response = [$response]");

      if (statusCode == 200) {
        pokeball = Pokeball.fromJson(response.data);
        if (pokeball.pokemon != null) {
          logger.d("Pokemon $name capturado!");
        } else {
          logger.d("Pokemon $name escapou!");
        }
      } else {
        logger.e(
            "Erro ao requisitar pokemon $name no servidor, codigo do erro: $statusCode");
        throw PokemonServerException(
            "Erro ao requisitar pokemon $name no servidor, codigo do erro: $statusCode");
      }
    } catch (e) {
      logger.wtf(
          "Erro ao requisitar Pokemon $name no servidor, erro: $e, data: $data");
      throw PokemonServerException(
          "Erro ao requisitar pokemon no servidor: $e");
    }

    return pokeball;
  }

  void clearPokeballs() {
    repository.pokeballs.clear();
  }

  List<Pokeball> getPokeballs() {
    return repository.getPokeballs();
  }

  Future<List<Pokeball>?> getPokeballsFromServer() async {
    return repository.getPokeballsFromServer();
  }
}
