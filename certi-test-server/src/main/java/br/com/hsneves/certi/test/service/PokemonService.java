package br.com.hsneves.certi.test.service;

import java.util.List;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.entity.dto.PokemonReportDTO;

/**
 * 
 * @author Henrique Neves
 *
 */
public interface PokemonService extends BaseService<Pokemon, Long> {

	/**
	 * Inicializa os dados de pokemons.
	 */
	void init();

	/**
	 * Catpura um pokemon!
	 * 
	 * @param idPokemon
	 */
	Pokeball throwPokeball(String pokemon);
	
	/**
	 * Retorna a lista de pokemons com o total de capturas e a data da última captura de cada Pokemon
	 * 
	 * @return
	 */
	List<PokemonReportDTO> getReport();
	
	/**
	 * Retorna a lista de pokemons capturados, cada um em sua {@link Pokeball}
	 * 
	 * @return
	 */
	List<Pokeball> getPokeballs();

}
