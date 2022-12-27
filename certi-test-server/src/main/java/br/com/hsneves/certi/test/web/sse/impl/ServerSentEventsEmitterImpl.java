package br.com.hsneves.certi.test.web.sse.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.listener.PokemonCatchListener;
import br.com.hsneves.certi.test.web.sse.CertiTestSseEmitter;
import br.com.hsneves.certi.test.web.sse.ServerSentEventMessageBuilder;
import br.com.hsneves.certi.test.web.sse.ServerSentEventMessageType;
import br.com.hsneves.certi.test.web.sse.ServerSentEventsComponent;
import br.com.hsneves.certi.test.web.sse.ServerSentEventsEmitter;

/**
 * Emissor de SSE
 * 
 * @author Henrique Neves
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServerSentEventsEmitterImpl implements ServerSentEventsEmitter, PokemonCatchListener {

	private static final Logger logger = LoggerFactory.getLogger(ServerSentEventsEmitterImpl.class);

	private Map<Long, CertiTestSseEmitter> emitterCache = new ConcurrentHashMap<>();

	@Autowired
	private ServerSentEventsComponent serverSentEventsComponent;

	@Value("${server.sent.events.timeout:2}")
	private long sseEmitterTimeout;

	public ServerSentEventsEmitterImpl() {
		super();
	}

	@Override
	public void destroy(Long clientId) {

		logger.info("destroy() - clientId = [" + clientId + "]");

		if (this.emitterCache.keySet().contains(clientId)) {
			SseEmitter emitter = this.emitterCache.get(clientId);
			if (emitter != null) {
				emitter.complete();
			}
			this.emitterCache.remove(clientId);
		}
	}

	@PreDestroy
	public void destroy() {

		logger.info("destroy()");

		for (Long clientId : this.emitterCache.keySet()) {
			unsubscribeListener(clientId);
			SseEmitter emitter = this.emitterCache.get(clientId);
			if (emitter != null) {
				emitter.complete();
			}
			this.emitterCache.remove(clientId);
		}
	}

	@Override
	public void onPokemonCatch(Pokeball pokeball) {
		logger.info("onPokemonCatch() - Pokebola retornou! pokemon = [" + pokeball.getPokemon() + "]");
		sendEventPokemonCatch(pokeball);
	}

	/**
	 * 
	 */
	private void subscribeListener(Long clientId) {
		this.serverSentEventsComponent.subscribePokemonCatchListener(clientId, this);
	}

	/**
	 * 
	 */
	private void unsubscribeListener(Long clientId) {
		this.serverSentEventsComponent.unsubscribePokemonCatchListener(clientId, this);
	}

	/**
	 * 
	 */
	private void unsubscribeAllListeners() {
		logger.info("Removendo todos os listeners..");
		for (Long clientId : this.emitterCache.keySet()) {
			unsubscribeListener(clientId);
		}
	}

	@Override
	public SseEmitter getEmitter(Long clientId) {

		logger.info("getEmitter() - clientId = [" + clientId + "]");

		CertiTestSseEmitter sseEmitter = this.emitterCache.get(clientId);
		if (sseEmitter != null) {
			complete(sseEmitter);
		}

		sseEmitter = new CertiTestSseEmitter(getSseEmitterTimeout(), clientId);
		setListeners(sseEmitter);

		this.emitterCache.put(clientId, sseEmitter);
		subscribeListener(clientId);

		// envia mensagem de notificação de conexão após 2 segundos
		ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		ex.schedule(() -> sendEventConnected(clientId), 2, TimeUnit.SECONDS);

		return sseEmitter;
	}

	/**
	 * 
	 * @return
	 */
	private long getSseEmitterTimeout() {
		return this.sseEmitterTimeout * 60 * 60 * 1000L;
	}

	/**
	 * Atribui os listeners para controle da conexão no canal de comunicação
	 * 
	 * @param sseEmitter
	 */
	private void setListeners(CertiTestSseEmitter sseEmitter) {
		sseEmitter.onTimeout(() -> {
			logger.info("setListeners() - SSEEmitter.onTimeout() - clientId = [" + sseEmitter.getClientId() + "]");
			this.emitterCache.remove(sseEmitter.getClientId());
		});
		sseEmitter.onError(t -> {
			logger.info("setListeners() - SSEEmitter.onError() - clientId = [" + sseEmitter.getClientId() + "], error = [" + t.getMessage() + "]");
			this.emitterCache.remove(sseEmitter.getClientId());
		});
		sseEmitter.onCompletion(() -> {
			logger.info("setListeners() - SSEEmitter.onCompletion() - clientId = [" + sseEmitter.getClientId() + "]");
			this.emitterCache.remove(sseEmitter.getClientId());
		});

	}

	/**
	 * 
	 * @param emitter
	 */
	public void complete(CertiTestSseEmitter emitter) {
		if (emitter != null) {
			emitter.complete();
		}
	}

	/**
	 * Fecha o canal e remove os listeners
	 * 
	 * @param emitter
	 */
	private void close(CertiTestSseEmitter emitter) {
		if (emitter != null) {
			emitter.complete();
			this.emitterCache.remove(emitter.getClientId());
		}
		if (this.emitterCache.size() == 0) {
			unsubscribeAllListeners();
		}
		emitter = null;
	}

	/**
	 * Envia o evento de confirmação da conexão
	 * 
	 * @param clientId id do cliente
	 */
	private void sendEventConnected(Long clientId) {

		logger.info("sendEventConnected() - clientId = [" + clientId + "]");

		if (this.emitterCache.containsKey(clientId)) {

			CertiTestSseEmitter emitter = this.emitterCache.get(clientId);

			String json = new ServerSentEventMessageBuilder().withType(ServerSentEventMessageType.CONNECTED).buildJson();

			logger.info("sendEventConnected() - enviando mensagem = [" + json + "], clientId = [" + clientId + "]");

			try {
				emitter.send(json);
			} catch (IOException e) {
				e.printStackTrace();
				close(emitter);
				logger.error("sendEventConnected() - falha ao enviar mensagem de notificação de conexão, clientId = [" + clientId + "]," + " error = [" + e.getMessage() + "]");
			}

		} else {

			logger.error("sendEventConnected() - cliente não encontrado no cache, clientId = [" + clientId + "]");
		}
	}

	/**
	 * Envia o evento para todos os clientes de retorno do lançamento da pokebola
	 * 
	 * @param pokemon
	 */
	private void sendEventPokemonCatch(Pokeball pokeball) {

		try {

			for (Long clientId : this.emitterCache.keySet()) {

				logger.info("sendEventPokemonCatch() - pokeball = [" + pokeball + "], clientId = [" + clientId + "]");

				CertiTestSseEmitter emitter = this.emitterCache.get(clientId);

				if (emitter != null) {

					String json = new ServerSentEventMessageBuilder().withType(ServerSentEventMessageType.POKEMON_CAUGHT).withData(pokeball).buildJson();

					try {

						logger.debug("sendEventPokemonCatch() - enviando mensagem de captura de Pokemon, clientId = [" + clientId + "], message = [" + json + "]");

						emitter.send(json);

					} catch (IllegalStateException | ClientAbortException e) {
						close(emitter);
						logger.error("sendEventPokemonCatch() - falha ao enviar mensagem de notificação de captura de Pokemon, clientId = [" + clientId + "]," + " error = ["
								+ e.getMessage() + "]");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sendEventPokemonCatch() - falha ao enviar mensagem de notificação de captura de Pokemon, error = [" + e.getMessage() + "]");
		}
	}

}