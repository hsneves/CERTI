package br.com.hsneves.certi.test.obj.builder;

import org.apache.commons.lang3.StringUtils;

import br.com.hsneves.certi.test.entity.User;
import br.com.hsneves.certi.test.enums.UserRole;
import br.com.hsneves.certi.test.exceptions.ObjBuilderException;

/**
 * 
 * Builder para construção do objeto {@link br.com.hsneves.certi.test.entity.User}
 * 
 * @author Henrique Neves
 *
 */
public class UserBuilder {

	private User user = new User();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public UserBuilder withName(String name) {
		this.user.setName(name);
		return this;
	}
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	public UserBuilder withRole(UserRole role) {
		this.user.setRole(role);
		return this;
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public UserBuilder withUsername(String username) {
		this.user.setUsername(username);
		return this;
	}
	
	/**
	 * 
	 * @param rawPassword
	 * @return
	 */
	public UserBuilder withRawPassword(String rawPassword) {
		this.user.setRawPassword(rawPassword);
		return this;
	}
	
	/**
	 * 
	 * @param active
	 * @return
	 */
	public UserBuilder withActive(boolean active) {
		this.user.setActive(active);
		return this;
	}
	
	/**
	 * Constrói um objeto do tipo {@link br.com.hsneves.certi.test.entity.User}
	 * 
	 * @return
	 */
	public User build() {
		if (StringUtils.isEmpty(this.user.getName()) || this.user.getRole() == null || StringUtils.isEmpty(this.user.getRawPassword())) {
			throw new ObjBuilderException();
		}
		return this.user;
	}
}
