const String host = "http://localhost:3000/certi";
const String urlPokemonCaughtSse = "$host/sse/";
const String urlCatchPokemon = "$host/rest/pokemon/catch/";
const String urlPokeballs = "$host/rest/pokemon/pokeballs/";
const String urlPokemonImage = "$host/img/pokemons/";
const String urlEmptyPokeball = "$host/img/empty_pokeball.png";

const String sseMessageTypeConnected = "CONNECTED";
const String sseMessageTypePokemonCaught = "POKEMON_CAUGHT";

const int sseConnecting = 1;
const int sseConnected = 2;
const int sseDisconnected = 3;
