package br.com.hsneves.certi.test.repository;

import java.util.Optional;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.hsneves.certi.test.CertiTestApplication;
import br.com.hsneves.certi.test.config.CertiCtaJpaTestConfig;
import br.com.hsneves.certi.test.entity.Pokemon;

/**
 * 
 * @author Henrique Neves
 *
 */
@RunWith(SpringRunner.class)
@PropertySource("application-test.properties")
@SpringBootTest(classes = { CertiTestApplication.class, CertiCtaJpaTestConfig.class })
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // importante para os testes executarem em ordem
public class PokemonRepositoryIntegrationTest {

	@Autowired
	private PokemonRepository pokemonRepository;

	@Test
	public void test2FindByName() {

		Optional<Pokemon> pokemon = this.pokemonRepository.findByName("pikachu");

		Assert.assertTrue(pokemon.isPresent());
		Assert.assertEquals(pokemon.get().getName(), "Pikachu");

		pokemon = this.pokemonRepository.findByName("PIkaChU");
		Assert.assertTrue(pokemon.isPresent());
		Assert.assertEquals(pokemon.get().getName(), "Pikachu");

		pokemon = this.pokemonRepository.findByName(null);
		Assert.assertFalse(pokemon.isPresent());

		pokemon = this.pokemonRepository.findByName("ddxxx");
		Assert.assertFalse(pokemon.isPresent());

		pokemon = this.pokemonRepository.findByName("pikach");
		Assert.assertFalse(pokemon.isPresent());
	}

}
