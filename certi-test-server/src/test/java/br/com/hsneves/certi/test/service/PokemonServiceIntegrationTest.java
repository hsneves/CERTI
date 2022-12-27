package br.com.hsneves.certi.test.service;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.hsneves.certi.test.CertiTestApplication;
import br.com.hsneves.certi.test.config.CertiCtaJpaTestConfig;
import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.entity.dto.PokemonReportDTO;
import br.com.hsneves.certi.test.utils.Constants;

/**
 * 
 * Testes unitários para {@link PokemonService}
 * 
 * @author Henrique Neves
 *
 */
@RunWith(SpringRunner.class)
@PropertySource("application-test.properties")
@SpringBootTest(classes = { CertiTestApplication.class, CertiCtaJpaTestConfig.class })
@ActiveProfiles(Constants.PROFILE_TEST)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // importante para os testes executarem em ordem
public class PokemonServiceIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(PokemonServiceIntegrationTest.class);

	@Autowired
	private PokemonService pokemonService;

	@Test
	public void test1Init() {
		
		logger.info("test1Init() - recuperando todos os pokemons cadastrados...");

		List<Pokemon> all = this.pokemonService.findAll();

		// deve inicializar com 151 pokemons
		Assert.assertEquals(all.size(), 151);
		
		logger.info("test1Init() - teste executado com sucesso! total de pokemons = [" + all.size() + "]");
	}

	@Test
	public void test2CatchPokemon() {

		logger.info("test2CatchPokemon() - capturando 1 pikachu, 1 vazia e 2 bulbasaur...");
		
		Pokeball pokeball = this.pokemonService.throwPokeball("pikachu");

		Assert.assertNotNull(pokeball);
		Assert.assertNotNull(pokeball.getPokemon());
		Assert.assertEquals("Pikachu", pokeball.getPokemon().getName());

		pokeball = this.pokemonService.throwPokeball("pikapi");

		Assert.assertNotNull(pokeball);
		Assert.assertNull(pokeball.getPokemon());

		pokeball = this.pokemonService.throwPokeball("bulbasaur");
		Assert.assertNotNull(pokeball);
		Assert.assertNotNull(pokeball.getPokemon());
		Assert.assertEquals("Bulbasaur", pokeball.getPokemon().getName());

		pokeball = this.pokemonService.throwPokeball("bulbasaur");
		Assert.assertNotNull(pokeball);
		Assert.assertNotNull(pokeball.getPokemon());
		Assert.assertEquals("Bulbasaur", pokeball.getPokemon().getName());
		
		logger.info("test2CatchPokemon() - teste executado com sucesso!");
	}

	//@Test
	public void test3GetPokeballs() {
		
		logger.info("test3GetPokeballs() - recuperando as pokebolas de pokemons capturados...");

		// Importante! teste depende da quantidade pokemons capturados em
		// testCatchPokemon()

		List<Pokeball> pokeballs = this.pokemonService.getPokeballs();

		System.out.println(pokeballs.toString());

		Assert.assertEquals(3, pokeballs.size());

		// Resultado deve retornar na ordem do último pokemon capturado
		Assert.assertEquals("Bulbasaur", pokeballs.get(0).getPokemon().getName());
		Assert.assertEquals("Bulbasaur", pokeballs.get(1).getPokemon().getName());
		Assert.assertEquals("Pikachu", pokeballs.get(2).getPokemon().getName());
		
		logger.info("test3GetPokeballs() - teste executado com sucesso!");
	}

	//@Test
	public void test4GetReport() {

		logger.info("test4GetReport() - recuperando relatório de todos os pokemons, suas capturas e quantidades capturada...");
		
		List<PokemonReportDTO> report = this.pokemonService.getReport();

		Assert.assertEquals(report.size(), 151);

		// data de captura de pikachu e bulbasaur não deve ser null
		PokemonReportDTO pikachu = null;
		PokemonReportDTO bulbasaur = null;

		Iterator<PokemonReportDTO> it = report.iterator();

		while (it.hasNext()) {
			PokemonReportDTO dto = it.next();
			if (dto.getPokemon().getName().equals("Pikachu")) {
				pikachu = dto;
				it.remove();
			}
			if (dto.getPokemon().getName().equals("Bulbasaur")) {
				bulbasaur = dto;
				it.remove();
			}
		}

		// data da última captura diferente de null
		Assert.assertNotNull(pikachu.getLastCatch());
		Assert.assertNotNull(bulbasaur.getLastCatch());

		// quantidade de capturas
		Assert.assertEquals(pikachu.getTotalCatch(), 1);
		Assert.assertEquals(bulbasaur.getTotalCatch(), 2);

		// restante não deve ter captura
		for (PokemonReportDTO dto : report) {
			Assert.assertNull(dto.getLastCatch());
			Assert.assertEquals(dto.getTotalCatch(), 0);
		}

		logger.info("test4GetReport() - teste executado com sucesso!");
	}
}
