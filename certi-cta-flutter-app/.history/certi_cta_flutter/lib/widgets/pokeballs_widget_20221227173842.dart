import 'package:certi_cta_flutter/services/pokemon_service.dart';
import 'package:certi_cta_flutter/widgets/pokeballs_list_state.dart';
import 'package:flutter/material.dart';

class PokeballsWidget extends StatefulWidget {
  final PokemonService pokemonService;

  late State<StatefulWidget> state;

  PokeballsWidget(this.pokemonService, {super.key}) {
    state = createState();
  }

  State<StatefulWidget> getState() {
    return state;
  }

  @override
  State<StatefulWidget> createState() {
    // ignore: no_logic_in_create_state
    return state;
  }
}
