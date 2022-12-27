package br.com.hsneves.certi.test.builder;

import org.junit.Assert;
import org.junit.Test;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.exceptions.ObjBuilderException;
import br.com.hsneves.certi.test.obj.builder.PokeballBuilder;

/**
 * Testes unitários para construtor de objetos {@link Pokeball}
 * 
 * @author Henrique Neves
 *
 */
public class PokeballBuilderTest {

	@Test
	public void testBuild() {

		Pokemon pokemon = new Pokemon();
		pokemon.setId(1L);
		pokemon.setName("Pikachu");

		Pokeball pokeball = new PokeballBuilder().withPokemon(pokemon).build();

		Assert.assertNotNull(pokeball);
		Assert.assertEquals(pokeball.getPokemon(), pokemon);

	}

	@Test(expected = ObjBuilderException.class)
	public void testBuildException() {
		new PokeballBuilder().build();
	}
	
	@Test
	public void testBuildEmptyPokeball() {
		Pokeball pokeball = new PokeballBuilder().buildEmptyPokeball();
		Assert.assertNotNull(pokeball);
		Assert.assertNull(pokeball.getPokemon());
	}

}
