package br.com.hsneves.certi.test.web.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 
 * @author Henrique Neves
 *
 */
public class CertiTestSseEmitter extends SseEmitter {

	private final Long clientId;

	public CertiTestSseEmitter(long timeout, Long clientId) {
		super(timeout);
		this.clientId = clientId;
	}

	/**
	 * 
	 * @return
	 */
	public Long getClientId() {
		return clientId;
	}

}
