import 'package:certi_cta_flutter/model/pokemon.dart';

class Pokeball {
  final int id;
  Pokemon? pokemon;
  final DateTime ts;

  Pokeball.withoutPokemon(this.id, this.ts);

  Pokeball({required this.id, required this.pokemon, required this.ts});

  factory Pokeball.empty() {
    return Pokeball.withoutPokemon(0, DateTime.now());
  }

  factory Pokeball.fromJson(Map<String, dynamic> json) {
    final Pokeball pokeball;
    if (json['pokemon'] != null) {
      Pokemon pokemon = Pokemon.fromJson(json['pokemon']);
      pokeball = Pokeball(id: json['id'], pokemon: pokemon, ts: DateTime.now());
    } else {
      pokeball = Pokeball.empty();
    }
    return pokeball;
  }
}
