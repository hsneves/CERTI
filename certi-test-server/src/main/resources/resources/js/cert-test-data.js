
const HTTP_METHOD_GET = 'GET';
const HTTP_METHOD_POST = 'POST';

const CONTENT_TYPE_JSON = 'application/json';

/**
 *	Executa um GET ajax executando a função de callback ao final do processo
 */
function ajaxGet(rest, callback) {
	$.ajax({
	    type: HTTP_METHOD_GET,
	    contentType: CONTENT_TYPE_JSON,
	    url: REST_HOST + rest,
	    cache: false,
	    timeout: AJAX_TIMEOUT,
	    success: function (data) {
			if (data != null && data.toString().trim().length > 0) {
				callback(data);	
			} else {
				callback(null);
			}
	    },
	    error: function (request, status, error) {
	    	let errorMsg = request.responseText != null ? request.responseText : request.statusText;
	      	showAlertMessageError($('#list-message'), 'Erro ao carregar de ' + rest + ': ' + errorMsg);
	    }
	});	
}

/**
 *	Retorna a lista de usuários cadastrados no sistema
 */
function getUsers(callback) {
	ajaxGet('/rest/users', callback);
}

/**
 *	Retorna o usuário logado
 */
function getLoggedUser(callback) {
	ajaxGet('/rest/users/logged', callback);
}