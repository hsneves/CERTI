import 'dart:convert';

import 'package:certi_cta_flutter/repositories/pokeball_repository.dart';
import 'package:certi_cta_flutter/services/pokemon_service.dart';
import 'package:certi_cta_flutter/utils/constants.dart';
import 'package:dio/dio.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http_mock_adapter/http_mock_adapter.dart';
import 'package:mockito/mockito.dart';

class DioMock extends Mock implements Dio {}

const String pokeballJson = """{"id":4,"pokemon":{"id":25,"name":"Pikachu"}}""";
const String emptyPokeballJson = """{"id":-999}""";

main() {
  final dio = Dio(BaseOptions());
  final dioAdapter = DioAdapter(dio: dio);

  setUp(() {
    dio.httpClientAdapter = dioAdapter;
  });

  final PokeballRepository pokemonRepository = PokeballRepository(dio);
  final PokemonService pokemonService = PokemonService(pokemonRepository, dio);

  group('Testes Pokemon Service', () {
    test('deve capturar o Pikachu', () async {
      dioAdapter.onGet(
        "${urlCatchPokemon}pikachu",
        (request) {
          return request.reply(200, json.decode(pokeballJson));
        },
        data: null,
        queryParameters: {},
        headers: {},
      );

      final pokeball = await pokemonService.throwPokeball("pikachu");
      expect(pokeball?.id, 4);
      expect(pokeball?.pokemon?.name, "Pikachu");
      expect(pokeball?.pokemon?.id, 25);
    });
    test('pokebola deve voltar vazia', () async {
      String pokemonName = "xyz";
      String path = "${urlCatchPokemon}${pokemonName}";

      dioAdapter.onGet(
        path,
        (request) {
          return request.reply(200, json.decode(emptyPokeballJson));
        },
        data: null,
        queryParameters: {},
        headers: {},
      );

      final pokeball = await pokemonService.throwPokeball(pokemonName);
      expect(pokeball?.pokemon, null);
    });
  });
}
