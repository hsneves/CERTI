package br.com.hsneves.certi.test.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.hsneves.certi.test.entity.Pokeball;
import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.entity.dto.PokemonReportDTO;
import br.com.hsneves.certi.test.obj.builder.PokeballBuilder;
import br.com.hsneves.certi.test.obj.builder.PokemonReportDTOBuilder;
import br.com.hsneves.certi.test.repository.PokeballRepository;
import br.com.hsneves.certi.test.repository.PokemonRepository;
import br.com.hsneves.certi.test.service.BaseServiceImpl;
import br.com.hsneves.certi.test.service.PokemonService;
import br.com.hsneves.certi.test.web.sse.ServerSentEventsComponent;

/**
 * 
 * @author Henrique Neves
 *
 */
@Service
public class PokemonServiceImpl extends BaseServiceImpl<PokemonRepository, Pokemon, Long> implements PokemonService {

	private static final Logger logger = LoggerFactory.getLogger(PokemonServiceImpl.class);

	@Autowired
	private PokeballRepository pokeballRepository;

	@Autowired
	private ServerSentEventsComponent serverSentEventsComponent;

	public PokemonServiceImpl(PokemonRepository repository) {
		super(repository);
	}

	@Override
	public void init() {

		if (getRepository().count() == 0) {

			Gson gson = new GsonBuilder().create();
			try {
				File data = ResourceUtils.getFile("classpath:data/pokemon_list.json");
				String json = FileUtils.readFileToString(data, "UTF-8");
				Pokemon[] pokemons = gson.fromJson(json, Pokemon[].class);
				logger.info("Inicalizando dados de pokemons, total de pokemons = [" + pokemons.length + "]");
				getRepository().saveAll(Arrays.asList(pokemons));
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Erro ao recuperar dados de pokemon na inicialização: " + e.getMessage());
			}
		} else {

			logger.debug("Dados de Pokemons já inicializados");
		}
	}

	@Override
	protected void entityPropertiesUpdate(Pokemon entity, Pokemon newEntity) {
		// do nothing
	}

	@Override
	public Pokeball throwPokeball(String name) {

		logger.info("catchPokemon() - Lançando pokebola para capturar o pokemon... [" + name + "]");

		Pokeball pokeball;

		Optional<Pokemon> opt = getRepository().findByName(name);

		if (opt.isPresent()) {
			Pokemon pokemon = opt.get();
			logger.info("catchPokemon() - Pokemon Capturado! [" + pokemon + "]");
			pokeball = this.pokeballRepository.save(new PokeballBuilder().withPokemon(pokemon).build());
		} else {
			logger.error("catchPokemon() - Pokemon '" + name + "' não encontrado!");
			pokeball = new PokeballBuilder().buildEmptyPokeball();
		}

		this.serverSentEventsComponent.notifyPokemonCatch(pokeball);
		return pokeball;
	}

	@Override
	public List<PokemonReportDTO> getReport() {

		List<Pokemon> pokemons = findAll();
		List<PokemonReportDTO> report = new ArrayList<>(pokemons.size());

		for (Pokemon pokemon : pokemons) {
			int totalCatch = this.pokeballRepository.getCountCatch(pokemon.getId());
			Date lastCatch = this.pokeballRepository.getLastCatch(pokemon.getId());
			PokemonReportDTO dto = new PokemonReportDTOBuilder().withPokemon(pokemon).withTotalCatch(totalCatch).withLastCatch(lastCatch).build();
			report.add(dto);
		}

		return report;
	}

	@Override
	public List<Pokeball> getPokeballs() {
		return this.pokeballRepository.getPokeballs();
	}
}
