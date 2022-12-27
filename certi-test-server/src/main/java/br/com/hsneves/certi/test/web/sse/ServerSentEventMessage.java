package br.com.hsneves.certi.test.web.sse;

/**
 * 
 * @author Henrique Neves
 *
 */
public class ServerSentEventMessage {

	private final ServerSentEventMessageType type;

	private final Object data;

	public ServerSentEventMessage(ServerSentEventMessageType type, Object data) {
		super();
		this.type = type;
		this.data = data;
	}

	public ServerSentEventMessageType getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

}
