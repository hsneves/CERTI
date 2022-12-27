package br.com.hsneves.certi.test.obj.builder;

import java.util.Date;

import br.com.hsneves.certi.test.entity.Pokemon;
import br.com.hsneves.certi.test.entity.dto.PokemonReportDTO;
import br.com.hsneves.certi.test.exceptions.ObjBuilderException;

/**
 * 
 * @author Henrique Neves
 *
 */
public class PokemonReportDTOBuilder {

	private PokemonReportDTO dto = new PokemonReportDTO();
	
	/**
	 * 
	 * @param pokemon
	 * @return
	 */
	public PokemonReportDTOBuilder withPokemon(Pokemon pokemon) {
		this.dto.setPokemon(pokemon);
		return this;
	}
	
	/**
	 * 
	 * @param total
	 * @return
	 */
	public PokemonReportDTOBuilder withTotalCatch(int total) {
		this.dto.setTotalCatch(total);
		return this;
	}
	
	/**
	 * 
	 * @param lastCatch
	 * @return
	 */
	public PokemonReportDTOBuilder withLastCatch(Date lastCatch) {
		this.dto.setLastCatch(lastCatch);
		return this;
	}
	
	/**
	 * Constrói um objeto do tipo {@link br.com.hsneves.certi.test.entity.dto.PokemonReportDTO}
	 * 
	 * @return
	 */
	public PokemonReportDTO build() {
		if (this.dto.getPokemon() == null) {
			throw new ObjBuilderException();
		}
		return this.dto;
	}
}
