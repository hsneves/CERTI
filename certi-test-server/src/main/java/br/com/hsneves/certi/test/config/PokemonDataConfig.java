package br.com.hsneves.certi.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.hsneves.certi.test.service.PokemonService;

/**
 * 
 * @author Henrique Neves
 *
 */
@Component
@Order(0)
public class PokemonDataConfig implements ApplicationRunner {

	@Autowired
	private PokemonService pokemonService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.pokemonService.init();
	}

}
