package br.com.hsneves.certi.test.listener;

import br.com.hsneves.certi.test.entity.Pokeball;

/**
 * 
 * @author Henrique Neves
 *
 */
public interface PokemonCatchListener {

	/**
	 * 
	 * @param pokeball
	 */
	void onPokemonCatch(Pokeball pokeball);
}
