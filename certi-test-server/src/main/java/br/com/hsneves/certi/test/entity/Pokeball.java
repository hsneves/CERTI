package br.com.hsneves.certi.test.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entidade responsável pelo armazenamento de pokemons capturados.
 * 
 * @author Henrique Neves
 *
 */
@Entity
@Table(name = "pokeball")
public class Pokeball extends BaseEntity {

	private static final long serialVersionUID = 3740415958380613208L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date ts = new Date();

	@ManyToOne
	@JoinColumn(name = "id_pokemon", nullable = false)
	private Pokemon pokemon;

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	public void setPokemon(Pokemon pokemon) {
		this.pokemon = pokemon;
	}

}
