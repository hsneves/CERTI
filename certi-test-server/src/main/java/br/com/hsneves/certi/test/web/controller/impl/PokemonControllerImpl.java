package br.com.hsneves.certi.test.web.controller.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.entity.dto.PokemonReportDTO;
import br.com.hsneves.certi.test.service.PokemonService;
import br.com.hsneves.certi.test.web.controller.BaseControllerImpl;
import br.com.hsneves.certi.test.web.controller.PokemonController;

/**
 * Implementação do controller para serviços com a entidade {@link Pokemon}
 * 
 * @param pokemon
 * @return
 */
@RestController
public class PokemonControllerImpl extends BaseControllerImpl<PokemonService, Pokemon, Long> implements PokemonController {

	private static final Logger logger = LoggerFactory.getLogger(PokemonControllerImpl.class);

	@Autowired
	private PokemonService pokemonService;

	@Override
	public PokemonService getService() {
		return this.pokemonService;
	}

	@GetMapping("/pokemon/report")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Override
	public List<PokemonReportDTO> getReport() {
		logger.debug("getReport() - /pokemon/report");
		return this.pokemonService.getReport();
	}

	@GetMapping("/pokemon/pokeballs")
	@Override
	public List<Pokeball> getPokeballs() {
		logger.debug("getCaughtPokemons() - /pokemon/pokeballs");
		return this.pokemonService.getPokeballs();
	}

	@GetMapping("/pokemon/all")
	@Override
	public List<Pokemon> all() {
		logger.debug("all() - /pokemon");
		return super.all();
	}

	@GetMapping("/pokemon/catch/{name}")
	@Override
	public Pokeball catchPokemon(@PathVariable String name) {
		logger.debug("catchPokemon() - /pokemon/catch/{name} - name = [" + name + "]");
		return this.pokemonService.throwPokeball(name);
	}

}
