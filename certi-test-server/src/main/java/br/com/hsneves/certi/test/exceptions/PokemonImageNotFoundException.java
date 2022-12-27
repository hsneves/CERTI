package br.com.hsneves.certi.test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author Henrique Neves
 *
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PokemonImageNotFoundException extends CertiTestRuntimeException {

	private static final long serialVersionUID = -2598031223246745682L;

}
