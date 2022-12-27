
const Debug = {
	GENERAL: false,
	SSE: true,
	URL_BUILDER: false,
	MESSAGES: true
}

const URL_BUILDER = new CertiTestUrlBuilder(Debug.URL_BUILDER, $('#contextPath').text(), $('#restPath').text());
const URL_POKEMONS_REPORT = URL_BUILDER.getRestUrl('/pokemon/report');
const SSE_URL = URL_BUILDER.getRestUrl('/pokemon/sse');

const PokemonType = {
	ROCK: 'ROCK', 
	FLYING: 'FLYING', 
	GHOST: 'GHOST', 
	DARK: 'DARK', 
	GRASS: 'GRASS', 
	ICE: 'ICE', 
	FAIRY: 'FAIRY', 
	FIGHTING: 'FIGHTING', 
	PSYCHIC: 'PSYCHIC', 
	STEEL: 'STEEL', 
	BUG: 'BUG', 
	FIRE: 'FIRE', 
	POISON: 'POISON', 
	GROUND: 'GROUND', 
	DRAGON: 'DRAGON', 
	ELECTRIC: 'ELECTRIC', 
	NORMAL: 'NORMAL', 
	WATER: 'WATER'
}

const ServerServerEventMessageType = {
	CONNECTED: 'CONNECTED',
	POKEMON_CAUGHT: 'POKEMON_CAUGHT'
}

var pokemonsTable, sseConnection, sseClientId;

/**
 * Funcao de inicializacao do sistema
 */
function init() {

	// carrega os dados na tabela
	initPokemonsTable();
	
	// inicia o Server-Sent events para receber notificações push do servidor
	initServerSentEvents();
	
	// antes de sair da página destroy o emissor do Server-Sent Events para poupar recursos
	window.onbeforeunload = function() {
	    disposeSseConnection()
    };   
}

/**
 * Descarta o emissor do Server-Sent Events.
 */
function disposeSseConnection() {
	if (Debug.SSE) {
		console.log('SSE | descartando conexão SSE, connection = [' + sseConnection + '], clientId = [' + sseClientId + ']');
	}
	
	// descarta conexão cliente
	if (sseConnection != null) {
		sseConnection.dispose();
	}
	
	// descarta conexão servidor
	$.get(URL_BUILDER.getUrl('/sse/destroy/' + sseClientId));
}

/**
 * Inicializa o Server-Sent Events para receber notificações push do servidor
 */
function initServerSentEvents() {

	sseClientId = getSseClientId();
	
	let sseHost = URL_BUILDER.getUrl('/sse/' + sseClientId);
	
	sseConnection = new EventSource(sseHost);
	
	/*
	 EventSource.CONNECTING = 0;
	 EventSource.OPEN = 1;
	 EventSource.CLOSED = 2;
	 */				
	
	sseConnection.onopen = () => {
		let readyState = sseConnection.readyState;
		if (Debug.SSE) {
			console.log('SSE | conexão aberta, readyState = [' + readyState + ']');
		}
		handleSseConnection();
	};
	
	sseConnection.onerror = () => {
		if (Debug.SSE) {
			console.log('SSE | erro na conexão, readyState = [' + sseConnection.readyState + ']');
		}
		handleSseError();
	};
	
	sseConnection.onmessage = (evt) => {
		if (Debug.SSE) {
			console.log('SSE | onMessage() - evt = [' + JSON.stringify(evt.data) + ']');
		}
		handleSseMessage(evt.data);
	};
}

/**
 * Tratamento após a conexão no SSE
 */
function handleSseConnection() {

	// apresenta mensagem de sucesso
	new CertiTestMessages(Debug.MESSAGES, $('#global-message'), MessageType.SUCCESS, "Conectado").showMessage();
	
	// recarrega a tabela de pokemons
	if (pokemonsTable != null) {
		$('#pokemons-table').DataTable().ajax.reload();
	}
}

/**
 * Tratamento de erros no SSE
 */
function handleSseError() {

	// apresentar mensagem de erro
	new CertiTestMessages(Debug.MESSAGES, $('#global-message'), MessageType.ERROR, "Erro na conexão com o servidor").showPersistentMessage();

	// remove registros da tabela
	if (pokemonsTable != null) {
		pokemonsTable.clear().draw();
	}
}

/**
 * Tratamento da mensagem de pokemon capturado recebida
 */
function handleSseMessage(data) {

	if (Debug.SSE) {
		console.log('SSE | Mensagem recebida!', data);
	}
	
	if (data != null) {
	
		let message = JSON.parse(data);

		if (message.type == "POKEMON_CAUGHT") {
		
			let pokemon = message.data.pokemon;
	
			if (pokemon != null) {
			
				if (Debug.SSE) {
					console.log('SSE | Pokemon capturado!', JSON.stringify(pokemon));
				}			
		
				// recarregar tabela de pokemons
				$('#pokemons-table').DataTable().ajax.reload();
				
				// exibir mensagem que o pokemon foi capturado
				let msg = 'Pokemon ' + pokemon.name + ' capturado!';
				new CertiTestMessages(Debug.MESSAGES, $('#global-message'), MessageType.INFO, msg).showMessage();
				
			} else {
			
				if (Debug.SSE) {
					console.log('SSE | Pokebola vazia!');
				}			
			}
		}
	}
}

/**
 * Inicializa e carrega os dados da tabela de pokemons
 */
function initPokemonsTable() {

	// Botão para capturar Pokemon
	$('#pokemons-table').on('click', 'i.row-catch', function (e) {
		e.stopPropagation();
		let pokemonName = $(this).data('pokemon');
		$.ajax({
			type: 'GET',
			contentType: 'application/json',
			url: URL_BUILDER.getRestUrl('/pokemon/catch/' + pokemonName),
			cache: false,
			timeout: 5000,
			success: function (data) {
				// showAlertMessageSuccess($('#list-message'), 'Topics updated sucessfully');
			},
			error: function (request, status, error) {
				// showAlertMessageError($('#list-message'), request.responseText);
			}
		});
	} );

	// Carrega a tabela de pokemons na interface
	pokemonsTable = $('#pokemons-table').DataTable({

    	'language': {
    		'url' : 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json',
    		'emptyTable': 'Ainda não capturou nunhum pokemon'
    	},
    	
        "pageLength": 15,
        "lengthChange": false,
        "ajax": {
            "type": "GET",
            "url" : URL_POKEMONS_REPORT,
            'contentType': 'application/json',
            "error": function(xhr, error, thrown) {
              // showAlertMessageError($('#list-message'), 'Error while loading networks from ' + URL_POKEMONS_REPORT);
            }
        },        
        "sAjaxDataProp": "",
        "autoWidth" : true,
		
        'columns': [
		  
          {
              name: 'id',
              sortable: false,
              className: 'dt-body-right',
              'render': function (data, type, full, meta) {
                  return full.pokemon.id;
              }
          },          
		  
          {
              name: 'pokemon.img',
              sortable: false,
              searchable: false,
              className: 'dt-body-center',
              'render': function (data, type, full, meta) {
              	  let imgSrc = getUrlImagePokemon(full.pokemon);
                  return '<img src="' + imgSrc + '" width="22" height="22" alt="' + full.pokemon.name + '">';
              }
          },          
		  
          {
              name: 'pokemon.name',
              sortable: false,
              className: 'dt-body-left',
              'render': function (data, type, full, meta) {
                  return full.pokemon.name;
              }
          },      
		  
          {
              name: 'pokemon.type1',
              sortable: false,
              className: 'dt-body-left',
              'render': function (data, type, full, meta) {
                  return getPokemonType(full.pokemon.type1);
              }
          },      
		  
          {
              name: 'pokemon.type2',
              sortable: false,
              className: 'dt-body-left',
              'render': function (data, type, full, meta) {
                  return getPokemonType(full.pokemon.type2);
              }
          },
		  
          {
              name: 'totalCatch',
              sortable: false,
              className: 'dt-body-right',
              'render': function (data, type, full, meta) {
                  return full.totalCatch;
              }
          },
		  
          {
              name: 'lastCatch',
              sortable: false,
              searchable: false,
              className: 'dt-body-center',
              'render': function (data, type, full, meta) {
                  return full.lastCatch;
              }
          },
          
          {
             sortable: false,
             searchable: false,
             width: '10px',
             className: 'dt-body-center',
             "render": function (data, type, full, meta) {
                 return '<i class="fa fa-dot-circle-o row-catch btn-remove-button" title="Capturar" data-pokemon="' + full.pokemon.name + '"/>';
             }
          }
        ]       
     });
}

/**
 * Retorna a URL da imagem do pokemon
 */
function getUrlImagePokemon(pokemon) {
	return URL_BUILDER.getUrl('/img/pokemons/' + pokemon.id + '.png');
}

/**
 * Retorna o tipo de pokemon de acordo com o valor recebido na enumeração
 */
function getPokemonType(type) {
	if (type != null) {
		switch(type) {
			case PokemonType.ROCK: 
				return 'Pedra';
			case PokemonType.FLYING: 
				return 'Voador'; 
			case PokemonType.GHOST: 
				return 'Fantasma'; 
			case PokemonType.DARK: 
				return 'Sombrio'; 
			case PokemonType.GRASS: 
				return 'Grama'; 
			case PokemonType.ICE: 
				return 'Gelo'; 
			case PokemonType.FAIRY: 
				return 'Mágico'; 
			case PokemonType.FIGHTING: 
				return 'Lutador'; 
			case PokemonType.PSYCHIC: 
				return 'Psíquico'; 
			case PokemonType.STEEL: 
				return 'Metálico'; 
			case PokemonType.BUG: 
				return 'Inseto'; 
			case PokemonType.FIRE: 
				return 'Fogo'; 
			case PokemonType.POISON: 
				return 'Venenoso'; 
			case PokemonType.GROUND: 
				return 'Terra'; 
			case PokemonType.DRAGON: 
				return 'Dragão'; 
			case PokemonType.ELECTRIC: 
				return 'Elétrico'; 
			case PokemonType.NORMAL: 
				return 'Normal'; 
			case PokemonType.WATER: 
				return 'Aquático';
		}
	}
	return '-';
}

/**
 * Saida do sistema
 */
function logout() {
	window.location = URL_BUILDER.getUrl('/logout');
}

/**
 * Adiciona as funcoes de clique nos botoes
 */
$(function(){

	// Logout
    $('#button-exit').on('click', function(e){
        logout();
        e.preventDefault();
    });

	// Correção para backdrop do modal
    $(document).on('show.bs.modal', '.modal', function (event) {
    	
        var zIndex = 1040 + (10 * $('.modal:visible').length);
        
		if ($(this).attr('id') == 'edit-properties-modal' || $(this).attr('id') == 'confirm-modal') {
			zIndex += 10;
		}
		
		$(this).css('z-index', zIndex);
        setTimeout(function() {
            $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
        }, 0);
    });

    // Correção do backgrop no modal de edição
    $('#edit-modal').on('hidden.bs.modal', function () {

        let modalName = $('#edit-modal-name').val();
        let removeBackDrop = true;

        switch (modalName) {
            case 'modal-user-edit':
                removeBackDrop = false;
                break;
        }

        if (removeBackDrop) {
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }
        
    });

});

/**
 * Retorna um número randômico que será utilizado como identificador de cliente do Server-Sent Events
 */
function getSseClientId() {
    return Math.floor(Math.random() * 2147483647);
}

// Document ready
$(document).ready(function () {
	init();
});

// Global ajax error handler
$(document).ajaxError(function(event, xhr, settings, thrownError) {
	if (xhr.status == 500) {
		location.href = $('#contextPath').text() + '/login';
		return;	
	}
});