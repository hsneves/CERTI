package br.com.hsneves.certi.test.entity.dto;

import java.util.Date;
import java.util.Objects;

import br.com.hsneves.certi.test.entity.Pokemon;

/**
 * Relatório para a página principal contendo o
 * {@link br.com.hsneves.certi.test.entity.Pokemon}, total de vezes que foi
 * caputarado e a última captura.
 * 
 * @author Henrique Neves
 *
 */
public class PokemonReportDTO {

	private Pokemon pokemon;

	private int totalCatch;

	private Date lastCatch;

	public Pokemon getPokemon() {
		return pokemon;
	}

	public void setPokemon(Pokemon pokemon) {
		this.pokemon = pokemon;
	}

	public int getTotalCatch() {
		return totalCatch;
	}

	public void setTotalCatch(int total) {
		this.totalCatch = total;
	}

	public Date getLastCatch() {
		return lastCatch;
	}

	public void setLastCatch(Date lastCatch) {
		this.lastCatch = lastCatch;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pokemon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PokemonReportDTO other = (PokemonReportDTO) obj;
		return Objects.equals(pokemon, other.pokemon);
	}

}
