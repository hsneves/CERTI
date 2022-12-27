package br.com.hsneves.certi.test.web.sse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author Henrique Neves
 *
 */
public final class ServerSentEventMessageBuilder {

	private ServerSentEventMessageType type;
	
	private Object data;
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public ServerSentEventMessageBuilder withType(ServerSentEventMessageType type) {
		this.type = type;
		return this;
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 */
	public ServerSentEventMessageBuilder withData(Object data) {
		this.data = data;
		return this;
	}
	
	/**
	 * Constrói o objeto {@link ServerSentEventMessage} e retorna o JSON
	 * 
	 * @return
	 */
	public String buildJson() {
		Gson gson = new GsonBuilder().create();
		ServerSentEventMessage msg = new ServerSentEventMessage(this.type, this.data);
		return gson.toJson(msg); 
	}
	
}
