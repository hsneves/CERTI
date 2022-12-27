package br.com.hsneves.certi.test.web.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 
 * @author Henrique Neves
 *
 */
public interface ServerSentEventsEmitter {
	
	/**
	 * 
	 * @param clientId
	 * @return
	 */
	public SseEmitter getEmitter(Long clientId);
	
	/**
	 * 
	 * @param clientId
	 */
	public void destroy(Long clientId);

}