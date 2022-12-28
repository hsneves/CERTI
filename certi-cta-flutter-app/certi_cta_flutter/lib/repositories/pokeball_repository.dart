import 'package:certi_cta_flutter/events/pokeball_loaded_event.dart';
import 'package:certi_cta_flutter/main.dart';
import 'package:certi_cta_flutter/model/pokeball.dart';
import 'package:certi_cta_flutter/utils/constants.dart';
import 'package:dio/dio.dart';

import '../exceptions/pokemon_server_exception.dart';

// ignore: depend_on_referenced_packages
import 'package:logger/logger.dart';

class PokeballRepository {
  final Logger logger = Logger();

  final List<Pokeball> pokeballs = [];
  late final Dio dio;

  PokeballRepository([Dio? client]) {
    if (client == null) {
      dio = Dio();
    } else {
      dio = client;
    }
  }

  void addPokeball(Pokeball pokeball) {
    logger.i("Adicionando pokebola a lista de pokemons capturados!");
    pokeballs.insert(0, pokeball);
  }

  List<Pokeball> getPokeballs() {
    return pokeballs;
  }

  Future<List<Pokeball>> getPokeballsFromServer() async {
    // recupera as pokebolas no servidor (somente se a lista estiver vazia)
    if (pokeballs.isEmpty) {
      try {
        logger.d("Consumindo serviço REST na URL = [$urlPokeballs]");

        final response = await dio.get(urlPokeballs);
        if (response.statusCode == 200) {
          final list = response.data as List;
          logger.d("Pokebolas recuperadas, pokemons = [$list]");
          for (var json in list) {
            final pokeball = Pokeball.fromJson(json);
            pokeballs.add(pokeball);
          }
          eventBus.fire(PokeballLoadedEvent(pokeballs));
        }
      } catch (e) {
        throw PokemonServerException(
            "Erro ao acessar servidor para recuperar as pokebolas $e");
      }
    }
    return pokeballs;
  }
}
