/**
 * Constantes utilizada pela aplicação
 */
export class Constants {    

    public static ServerSentEventsMessageType = {
        CONNECTED: 'CONNECTED',
        POKEMON_CAUGHT: 'POKEMON_CAUGHT'
    }

    public static HOST = "localhost";
    public static PORT = "3000";
    public static CONTEXT_PATH = "certi";
    public static URL_HOST = "http://" + Constants.HOST + ":" + Constants.PORT +"/" + Constants.CONTEXT_PATH + "/";
    public static GET_POKEBALLS = Constants.URL_HOST + "rest/pokemon/pokeballs/";
    public static THROW_POKEBALL = Constants.URL_HOST + "rest/pokemon/catch/";
    public static SSE_POKEMONS_CAUGHT_CONNECT = Constants.URL_HOST + "sse/";
    public static SSE_POKEMONS_CAUGHT_DESTROY = Constants.URL_HOST + "sse/destroy/";
    public static SSE_MESSAGE_EVENT_BUS = "sse:message:event:bus";
    public static URL_POKEMON_IMAGE = Constants.URL_HOST + "img/pokemons/";
    public static URL_EMPTY_POKEBALL = Constants.URL_HOST + "img/empty_pokeball.png";
    public static HTTP_INTERCEPTOR_REST = "pokemons:http:exception:event:bus";
    public static SSE_CONNECTION_BUS = "pokemons:sse:exception:event:bus";
 }