import 'package:certi_cta_flutter/model/pokeball.dart';
import 'package:certi_cta_flutter/services/pokemon_service.dart';
import 'package:flutter/material.dart';

import '../utils/constants.dart';

class PokeballsWidget extends StatelessWidget {
  final PokemonService pokemonService;

  const PokeballsWidget(this.pokemonService, {super.key});

  void refreshListView() {}

  @override
  Widget build(BuildContext context) {
    if (pokemonService.getPokeballs().isEmpty) {
      return const Text('Ainda não capturou nunhum pokemon');
    } else {
      return Expanded(
        child: ListView.builder(
          itemCount: pokemonService.getPokeballs().length,
          itemBuilder: (context, index) {
            Pokeball pokeball = pokemonService.getPokeballs()[index];
            return ListTile(
                title: Text(pokeball.pokemon!.name),
                subtitle: Text("Id: ${pokeball.pokemon!.id}"),
                leading: CircleAvatar(
                    backgroundColor: const Color.fromRGBO(0, 0, 0, 0.15),
                    backgroundImage: NetworkImage(
                        "$urlPokemonImage${pokeball.pokemon!.id}.png")));
          },
        ),
      );
    }
  }
}
