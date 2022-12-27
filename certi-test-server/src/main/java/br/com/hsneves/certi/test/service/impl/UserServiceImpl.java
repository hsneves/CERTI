package br.com.hsneves.certi.test.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.hsneves.certi.test.entity.User;
import br.com.hsneves.certi.test.enums.UserRole;
import br.com.hsneves.certi.test.obj.builder.UserBuilder;
import br.com.hsneves.certi.test.repository.UserRepository;
import br.com.hsneves.certi.test.service.BaseServiceImpl;
import br.com.hsneves.certi.test.service.UserService;
import br.com.hsneves.certi.test.utils.Constants;

/**
 * 
 * @author Henrique Neves
 *
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserRepository, User, Long> implements UserService, UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	protected UserServiceImpl(UserRepository repository) {
		super(repository);
	}

	@Override
	public void init() {
		if (getRepository().count() == 0) {
			String name = Constants.DEFAULT_ADMIN_NAME;
			String username = Constants.DEFAULT_ADMIN_USERNAME;
			String password = Constants.DEFAULT_ADMIN_USERNAME;
			logger.info("Inicializando usuário Admnistrador, utilize username = [" + username + "], senha = [" + password + "] para entrar no sistema.");
			User admin = new UserBuilder().withName(name).withUsername(username).withRawPassword(password).withRole(UserRole.ADMIN).withActive(true).build();
			save(admin);
		} else {
			logger.debug("Banco de dados de usuários já inicializado.");
		}
		
	}

	@Override
	protected void preSave(User user) {
		super.preSave(user);
		
		// Encriptar password antes do armazenamento
		if (StringUtils.isNotBlank(user.getRawPassword()) && !user.getRawPassword().equals(Constants.BLANK_PASSWORD)) {
			user.setPassword(user.getRawPassword());
			String encodedPassword = this.passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
		}
	}

	@Override
	protected void entityPropertiesUpdate(User entity, User newEntity) {
		
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = getRepository().findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
		return user.get();
	}

}
