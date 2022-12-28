import 'package:certi_cta_flutter/model/pokeball.dart';
import 'package:certi_cta_flutter/services/pokemon_service.dart';
import 'package:certi_cta_flutter/utils/constants.dart';
import 'package:certi_cta_flutter/widgets/pokeballs_widget.dart';
import 'package:flutter/material.dart';

class PokeballsListState extends State<PokeballsWidget> {
  final PokemonService pokemonService;

  late List<Pokeball> pokeballs;

  PokeballsListState(this.pokemonService) {
    pokeballs = pokemonService.getPokeballs();
  }

  @override
  Widget build(BuildContext context) {
    if (pokemonService.getPokeballs().isEmpty) {
      return const Text('Ainda não capturou nunhum pokemon');
    } else {
      return Expanded(
        child: ListView.builder(
          physics: const AlwaysScrollableScrollPhysics(),
          itemCount: pokeballs.length,
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
