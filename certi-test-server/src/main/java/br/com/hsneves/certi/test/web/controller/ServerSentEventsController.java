package br.com.hsneves.certi.test.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 
 * @author Henrique Neves
 *
 */
public interface ServerSentEventsController {

	/**
	 * Inicializa a recepção de notificações via push do servidor
	 * 
	 * @param clientId
	 * @return
	 */
	SseEmitter sse(Long clientId);
	
	/**
	 * Destrói o serviço para economizar recursos
	 * 
	 * @param clientId
	 */
	ResponseEntity<Boolean> destroy(Long clientId);
}
