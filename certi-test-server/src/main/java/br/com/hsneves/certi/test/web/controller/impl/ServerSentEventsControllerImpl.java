package br.com.hsneves.certi.test.web.controller.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import br.com.hsneves.certi.test.web.controller.ServerSentEventsController;
import br.com.hsneves.certi.test.web.sse.impl.ServerSentEventsEmitterImpl;

/**
 * 
 * @author Henrique Neves
 *
 */
@Controller
@RequestMapping("/")
public class ServerSentEventsControllerImpl implements ServerSentEventsController {

	private static final Logger logger = LoggerFactory.getLogger(ServerSentEventsControllerImpl.class);

	@Autowired
	private ServerSentEventsEmitterImpl emitter;

	@GetMapping("/sse/{clientId}")
	@Override
	public SseEmitter sse(@PathVariable Long clientId) {

		logger.debug("sse() - /sse/{clientId} - clientId = [" + clientId + "]");

		SseEmitter sseEmitter = this.emitter.getEmitter(clientId);
		return sseEmitter;
	}

	@GetMapping("/sse/destroy/{clientId}")
	@Override
	public ResponseEntity<Boolean> destroy(@PathVariable Long clientId) {

		logger.debug("destroy() - /sse/destroy/{clientId} - clientId = [" + clientId + "]");

		if (clientId != null) {
			this.emitter.destroy(clientId);
		}
		return ResponseEntity.ok().body(Boolean.TRUE);
	}

}
