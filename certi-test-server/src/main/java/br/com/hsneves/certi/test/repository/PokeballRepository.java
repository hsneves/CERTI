package br.com.hsneves.certi.test.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.hsneves.certi.test.entity.Pokeball;

/**
 * Repósitorio para operações com o banco de dados na entidade {@link Pokeball}
 * 
 * @author Henrique Neves
 *
 */
@Repository
public interface PokeballRepository extends BaseRepository<Pokeball, Long> {

	/**
	 * Retorna a lista de pokebolas capturadas pelo treinador Pokemon
	 * 
	 * @return
	 */
	@Query("from Pokeball order by ts desc")
	List<Pokeball> getPokeballs();
	
	/**
	 * Consulta o total de vezes que o Pokemon foi capturado
	 * 
	 * @param idPokemon
	 * @return
	 */
	@Query("select count(*) from Pokeball where pokemon.id = ?1")
	int getCountCatch(Long idPokemon);
	
	/**
	 * Consulta a data/hora da última captura do Pokemon
	 * 
	 * @param idPokemon
	 * @return
	 */
	@Query("select max(ts) from Pokeball where pokemon.id = ?1")
	Date getLastCatch(Long idPokemon);
	
}
