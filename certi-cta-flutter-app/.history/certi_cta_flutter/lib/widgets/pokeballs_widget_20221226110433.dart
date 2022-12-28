import 'package:certi_cta_flutter/services/pokemon_service.dart';
import 'package:flutter/material.dart';

import '../utils/constants.dart';

class PokeballListView extends StatelessWidget {
  final PokemonService pokemonService;

  const PokeballListView(this.pokemonService, {super.key});

  void refreshListView() {}

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(8),
      children: <Widget>[
        ListTile(
            title: const Text("teste"),
            subtitle: const Text("Id: ${"999"}"),
            leading: const CircleAvatar(
                backgroundImage: NetworkImage("${urlPokemonImage}1.png")),
            trailing: const Icon(Icons.star)),

        /*for (var pokebola in pokemonService.getPokeballs())
          ListTile(
              title: Text(pokebola.pokemon!.name),
              subtitle: Text("Id: ${pokebola.pokemon!.id}"),
              leading: CircleAvatar(
                  backgroundImage: NetworkImage(
                      "${urlPokemonImage + pokebola.pokemon!.id.toString()}.png")),
              trailing: const Icon(Icons.star)),*/
      ],
    );
  }
}
