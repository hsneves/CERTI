package br.com.hsneves.certi.test.obj.builder;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.exceptions.ObjBuilderException;

/**
 * 
 * Builder para construção do objeto {@link br.com.hsneves.certi.test.entity.Pokeball}
 * 
 * @author Henrique Neves
 *
 */
public class PokeballBuilder {

	private Pokeball pokeball = new Pokeball();

	/**
	 * 
	 * @param pokemon
	 * @return
	 */
	public PokeballBuilder withPokemon(Pokemon pokemon) {
		this.pokeball.setPokemon(pokemon);
		return this;
	}

	/**
	 * Constróio um objeto do tipo {@link br.com.hsneves.certi.test.entity.Pokeball}
	 * 
	 * @return
	 */
	public Pokeball build() {
		if (this.pokeball.getPokemon() == null) {
			throw new ObjBuilderException();
		}
		return this.pokeball;
	}

	/**
	 * Constrói um objeto do tipo {@link br.com.hsneves.certi.test.entity.Pokeball} sem um pokemon.
	 * 
	 * @return
	 */
	public Pokeball buildEmptyPokeball() {
		this.pokeball.setId(-999L);
		return this.pokeball;
	}
}
