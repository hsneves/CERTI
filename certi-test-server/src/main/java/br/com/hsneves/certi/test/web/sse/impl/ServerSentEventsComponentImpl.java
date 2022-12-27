package br.com.hsneves.certi.test.web.sse.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.listener.PokemonCatchListener;
import br.com.hsneves.certi.test.web.sse.ServerSentEventsComponent;

/**
 * 
 * @author Henrique Neves
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServerSentEventsComponentImpl implements ServerSentEventsComponent {

	private static final Logger logger = LoggerFactory.getLogger(ServerSentEventsComponentImpl.class);

	private Set<PokemonCatchListener> listeners = new LinkedHashSet<>();

	private static final Object lock = new Object();

	@Override
	public void subscribePokemonCatchListener(Long clientId, PokemonCatchListener listener) {
		logger.info("subscribePokemonCatchListener() - Registrando novo listener de captura de pokemon, clientId = [" + clientId + "]");
		synchronized (lock) {
			this.listeners.add(listener);
		}
	}

	@Override
	public void unsubscribePokemonCatchListener(Long clientId, PokemonCatchListener listener) {
		logger.info("unsubscribePokemonCatchListener() - Removendo listener de captura de pokemon, clientId = [" + clientId + "]");
		synchronized (lock) {
			this.listeners.remove(listener);
		}
	}

	@Override
	public void notifyPokemonCatch(Pokeball pokeball) {

		logger.debug("notifyPokemonCatch() - notificando captura de Pokemon! pokemon = [" + pokeball.getPokemon() + "]");

		synchronized (lock) {
			for (PokemonCatchListener listener : this.listeners) {
				try {
					listener.onPokemonCatch(pokeball);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
