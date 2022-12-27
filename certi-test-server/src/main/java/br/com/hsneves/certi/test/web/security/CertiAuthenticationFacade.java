package br.com.hsneves.certi.test.web.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.hsneves.certi.test.entity.User;

/**
 * Facade para retorno do usuário logado
 * 
 * @author Henrique Neves
 *
 */
@Component
public class CertiAuthenticationFacade {

	/**
	 * Retorna o usuário logado.
	 * 
	 * @return
	 */
	public User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}