package br.com.hsneves.certi.test.web.sse;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.listener.PokemonCatchListener;

/**
 * 
 * @author Henrique Neves
 *
 */
public interface ServerSentEventsComponent {

	/**
	 * 
	 * @param clientId
	 * @param listener
	 */
	void subscribePokemonCatchListener(Long clientId, PokemonCatchListener listener);

	/**
	 * 
	 * @param clientId
	 * @param listener
	 */
	void unsubscribePokemonCatchListener(Long clientId, PokemonCatchListener listener);

	/**
	 * 
	 * @param pokeball
	 */
	void notifyPokemonCatch(Pokeball pokeball);

}
