package br.com.hsneves.certi.test.web.controller;

import java.util.List;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.entity.dto.PokemonReportDTO;
import br.com.hsneves.certi.test.enums.UserRole;
import br.com.hsneves.certi.test.exceptions.PokemonNotFoundException;

/**
 * Controller para {@link Pokemon}
 * 
 * @author Henrique Neves
 *
 */
public interface PokemonController {

	/**
	 * Retorna o relatório com as informações sobre o pokemon, quantas vezes foi
	 * capturado e a data/hora da última captura. Importante: somente usuários com a
	 * {@link UserRole.ADMIN} podem executar o método.
	 * 
	 * @return
	 */
	List<PokemonReportDTO> getReport();

	/**
	 * Retorna a lista de pokemons capturados.
	 * 
	 * @return
	 */
	List<Pokeball> getPokeballs();

	/**
	 * Captura um pokemon!
	 * 
	 * @param name Nome do Pokemon
	 * @return O Pokemon capturado
	 * @throws PokemonNotFoundException Caso não encontre o Pokemon lança uma
	 *                                  exceção que vai com o status HTTP =
	 *                                  HttpStatus.NOT_FOUND
	 *                                  {@link PokemonNotFoundException}
	 */
	Pokeball catchPokemon(String name);

}
