import 'dart:async';

import 'package:certi_cta_flutter/events/pokeball_loaded_event.dart';
import 'package:certi_cta_flutter/events/pokemon_caught_event.dart';
import 'package:certi_cta_flutter/repositories/pokeball_repository.dart';
import 'package:certi_cta_flutter/services/pokemon_service.dart';
import 'package:certi_cta_flutter/sse/pokemons_caught_sse.dart';
import 'package:certi_cta_flutter/utils/constants.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';

import 'package:flutter/services.dart';
import 'exceptions/pokemon_server_exception.dart';
import 'model/pokeball.dart';

// ignore: depend_on_referenced_packages
import 'package:logger/logger.dart';

// ignore: depend_on_referenced_packages
import 'package:event_bus/event_bus.dart';

final Logger logger = Logger();

// event bus para monitorar os pokemons que foram capturados através do SSE
final EventBus eventBus = EventBus();

// serviço para executar as operações de captura e retorno da lista de pokemons
final PokemonService pokemonService =
    PokemonService(PokeballRepository(), Dio());

void main() {
  runApp(const CertiCtaApp());
}

class CertiCtaApp extends StatelessWidget {
  const CertiCtaApp({super.key});

  @override
  Widget build(BuildContext context) {
    // recupera as pokebolas
    pokemonService.getPokeballsFromServer();

    // inicializa o cliente SSE
    PokemonCaughtSse().connect();

    // inicializa o app
    return MaterialApp(
      title: 'CERTI - Catch them all!',
      theme: ThemeData(
        primarySwatch: Colors.red,
      ),
      home: const CertiCtaHome(title: 'CERTI - Catch them all!'),
    );
  }
}

class CertiCtaHome extends StatefulWidget {
  const CertiCtaHome({super.key, required this.title});
  final String title;

  @override
  State<CertiCtaHome> createState() => _CertiCtaHomeState();
}

class _CertiCtaHomeState extends State<CertiCtaHome> {
  final throwPokeballController = TextEditingController();

  final Table table = Table();

  @override
  void initState() {
    eventBus.on<PokemonCaughtEvent>().listen((event) {
      logger.i(
          "Evento de captura de pokemon recebido pelo EventBus! Pokemon: ${event.pokeball.pokemon?.name}");
      // adiciona na lista de pokemons capturados
      pokemonService.addPokemonCaught(event.pokeball);
      // atualiza a tabela de pokemons capturados
      refreshPokemonCaughtTable();
    });
    eventBus.on<PokeballLoadedEvent>().listen((event) {
      logger.i(
          "Evento de carregamento de pokebolas recebido pelo EventBus! Pokemons ${event.pokeballs.length}");
      refreshPokemonCaughtTable();
    });
    super.initState();
  }

  void refreshPokemonCaughtTable() {
    logger.i("Regarregando tabela de pokemons capturados..");
    setState(() {});
  }

  @override
  void dispose() {
    throwPokeballController.dispose();
    PokemonCaughtSse().dispose();
    super.dispose();
  }

  Future<void> throwPokeball(String pokemonName) async {
    String? urlImage;
    String message;

    logger.i("Lançando pokebola para capturar o pokemon $pokemonName!");

    try {
      Pokeball? pokeball = await pokemonService.throwPokeball(pokemonName);
      if (pokeball?.pokemon != null) {
        String? id = pokeball?.pokemon?.id.toString();
        urlImage = "${urlPokemonImage + id!}.png";
        message = "Pokemon ${pokeball?.pokemon?.name} capturado!";
        logger.i(message);
        logger.i("Requisitando imagem do pokemon, url = [$urlImage]");
      } else {
        urlImage = urlEmptyPokeball;
        message = "Pokemon ${throwPokeballController.text} escapou!";
        logger.i(message);
        logger.i("Requisitando imagem de pokebola vazia, url = [$urlImage]");
      }
    } on PokemonServerException {
      urlImage = null;
      message = "Servidor indisponível";
      logger.e(message);
    }

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          content: Stack(
            alignment: Alignment.topCenter,
            children: <Widget>[
              IntrinsicHeight(
                  child: Row(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                    if (urlImage != null) ...[
                      Column(children: [
                        Image.network(urlImage),
                      ]),
                    ],
                    Column(
                      children: [
                        Text(
                          message,
                          // ignore: prefer_const_constructors
                          style: TextStyle(
                            fontSize: 24,
                          ),
                        )
                      ],
                    ),
                  ])),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: SafeArea(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            // TextField para capturar um pokemon
            TextFormField(
              controller: throwPokeballController,
              autofocus: true,
              obscureText: false,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: 'Digite o nome do pokemon...',
              ),
              keyboardType: TextInputType.name,
              inputFormatters: [
                FilteringTextInputFormatter.allow(RegExp('[a-zA-Z]'))
              ],
            ),

            // Botão para executar a captura do pokemon
            TextButton(
              style: ButtonStyle(
                overlayColor: MaterialStateProperty.resolveWith<Color?>(
                    (Set<MaterialState> states) {
                  if (states.contains(MaterialState.focused)) {
                    return Colors.white;
                  }
                  return null;
                }),
              ),
              onPressed: () {
                throwPokeball(throwPokeballController.text);
              },
              child: const Text('Lançar Pokebola!'),
            ),

            // Tabela de pokemons capturados
            Expanded(
              child: ListView.builder(
                itemCount: 10,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Text('item $index'),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
