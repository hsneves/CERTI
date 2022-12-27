package br.com.hsneves.certi.test.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import br.com.hsneves.certi.test.enums.PokemonType;

/**
 * 
 * Entidade responsável pelo armazenamento de Pokemons.
 * 
 * @author Henrique Neves
 *
 */
@Entity
@Table(name = "pokemon", indexes = { @Index(name = "idx_pokemon_name", columnList = "name") })
public class Pokemon extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7027436640670996291L;

	@Column(length = 128, nullable = false, unique = true)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private PokemonType type1;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private PokemonType type2;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PokemonType getType1() {
		return type1;
	}

	public void setType1(PokemonType type1) {
		this.type1 = type1;
	}

	public PokemonType getType2() {
		return type2;
	}

	public void setType2(PokemonType type2) {
		this.type2 = type2;
	}

}
