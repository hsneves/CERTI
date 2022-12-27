package br.com.hsneves.certi.test.service;

import br.com.hsneves.certi.test.entity.User;

/**
 * 
 * @author Henrique Neves
 *
 */
public interface UserService extends BaseService<User, Long> {

	/**
	 * Usado na primeira inicialização para adicionar o usuário admin/admin.
	 */
	void init();
	
}
