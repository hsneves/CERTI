package br.com.hsneves.certi.test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção disparada quando um pokemon não é encontrado.
 * 
 * @author Henrique Neves
 *
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PokemonNotFoundException extends CertiTestException {

	private static final long serialVersionUID = -3700635072758112314L;

}
