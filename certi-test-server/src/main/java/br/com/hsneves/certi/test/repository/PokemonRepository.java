package br.com.hsneves.certi.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.hsneves.certi.test.entity.Pokemon;

/**
 * Repósitorio para operações com o banco de dados na entidade Pokemon
 * 
 * @author Henrique Neves
 *
 */
@Repository
public interface PokemonRepository extends BaseRepository<Pokemon, Long> {

	/**
	 * Encontrar o pokemon pelo nome
	 * 
	 * @param name
	 * @return
	 */
	@Query("from Pokemon where lower(name) = lower(?1)")
	Optional<Pokemon> findByName(String name);

}
