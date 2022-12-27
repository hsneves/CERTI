package br.com.hsneves.certi.test.builder;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.entity.dto.PokemonReportDTO;
import br.com.hsneves.certi.test.exceptions.ObjBuilderException;
import br.com.hsneves.certi.test.obj.builder.PokemonReportDTOBuilder;

/**
 * Testes unitários para construtor de objetos {@link br.com.hsneves.certi.test.entity.dto.PokemonReportDTO}
 * 
 * @author Henrique Neves
 *
 */
public class PokemonReportDTOBuilderTests {

	@Test
	public void testBuild() {

		Pokemon pokemon = new Pokemon();
		pokemon.setId(1L);
		pokemon.setName("Pikachu");
		Date lastCatch = new Date();
		int totalCatch = 13;

		PokemonReportDTO report = new PokemonReportDTOBuilder()
				.withPokemon(pokemon)
				.withTotalCatch(totalCatch)
				.withLastCatch(lastCatch)
				.build();

		Assert.assertNotNull(report);
		Assert.assertEquals(report.getPokemon(), pokemon);
		Assert.assertEquals(report.getTotalCatch(), totalCatch);
		Assert.assertEquals(report.getLastCatch(), lastCatch);

	}

	@Test(expected = ObjBuilderException.class)
	public void testBuildException() {
		new PokemonReportDTOBuilder().build();
	}
}
