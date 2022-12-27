package br.com.hsneves.certi.test.repository;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.hsneves.certi.test.entity.Pokemon;

/**
 * Testes unitários do repositório {@link PokemonRepository}
 * 
 * @author Henrique Neves
 *
 */
@RunWith(SpringRunner.class)
public class PokemonRepositoryTest {

	@MockBean
	private PokemonRepository pokemonRepository;

	@Before
	public void setUp() {
		
		Pokemon pikachu = new Pokemon();
		pikachu.setId(99L);
		pikachu.setName("Pikachu");
		Optional<Pokemon> opt = Optional.of(pikachu);
		
		Mockito.when(this.pokemonRepository.findByName("pikachu")).thenReturn(opt);
		Mockito.when(this.pokemonRepository.findByName("xxx")).thenReturn(Optional.ofNullable(null));
	}

	/**
	 * Testar consulta de pokemon por nome
	 */
	@Test
	public void testFindByName() {

		Optional<Pokemon> pikachu = this.pokemonRepository.findByName("pikachu");
		Optional<Pokemon> xxx = this.pokemonRepository.findByName("xxx");
		
		Assert.assertTrue(pikachu.isPresent());
		Assert.assertFalse(xxx.isPresent());
		
		Assert.assertEquals(pikachu.get().getId(), new Long(99L));
		Assert.assertEquals(pikachu.get().getName(), "Pikachu");
		
	}
	
}
