import 'dart:convert';

import 'package:certi_cta_flutter/model/pokeball.dart';
import 'package:certi_cta_flutter/model/pokemon.dart';
import 'package:certi_cta_flutter/repositories/pokeball_repository.dart';
import 'package:certi_cta_flutter/utils/constants.dart';
import 'package:dio/dio.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http_mock_adapter/http_mock_adapter.dart';
import 'package:mockito/mockito.dart';

class DioMock extends Mock implements Dio {}

const String pokeballsJson = ''' 
[{"id":1,"pokemon":{"id":1,"name":"Bulbasaur"}},{"id":2,"pokemon":{"id":3,"name":"Venusaur"}},{"id":3,"pokemon":{"id":4,"name":"Charmander"}},{"id":4,"pokemon":{"id":25,"name":"Pikachu"}}]
''';

main() {
  final dio = Dio(BaseOptions());
  final dioAdapter = DioAdapter(dio: dio);

  setUp(() {
    dio.httpClientAdapter = dioAdapter;
    dioAdapter.onGet(
      urlPokeballs,
      (request) {
        return request.reply(200, json.decode(pokeballsJson));
      },
      data: null,
      queryParameters: {},
      headers: {},
    );
  });

  final PokeballRepository pokemonRepository = PokeballRepository(dio);

  group('Testes Pokemon Repository', () {
    test('deve recuperar a lista de pokemons capturados', () async {
      final pokeballs = await pokemonRepository.getPokeballsFromServer();
      expect(pokeballs.length, 4);
      expect(pokeballs[0].pokemon?.name, "Bulbasaur");
      expect(pokeballs[1].pokemon?.name, "Venusaur");
      expect(pokeballs[2].pokemon?.name, "Charmander");
      expect(pokeballs[3].pokemon?.name, "Pikachu");
    });

    test('deve adicionar o Squirtle!', () async {
      Pokeball squirtle = Pokeball(
          id: 999,
          pokemon: Pokemon(id: 888, name: "Squirtle"),
          ts: DateTime.now());
      final pokeballs = await pokemonRepository.getPokeballsFromServer();
      expect(pokeballs.length, 4);

      pokemonRepository.addPokeball(squirtle);
      final pokeballsSquirtle =
          await pokemonRepository.getPokeballsFromServer();
      expect(pokeballsSquirtle.length, 5);
      expect(pokeballsSquirtle[4].id, 999);
      expect(pokeballsSquirtle[4].pokemon?.id, 888);
      expect(pokeballsSquirtle[4].pokemon?.name, "Squirtle");
    });
  });
}
