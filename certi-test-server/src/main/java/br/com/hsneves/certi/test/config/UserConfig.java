package br.com.hsneves.certi.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.hsneves.certi.test.service.UserService;
import br.com.hsneves.certi.test.utils.Constants;

/**
 * 
 * @author Henrique Neves
 *
 */
@Component
@Order(1)
@Profile(Constants.PROFILE_PRODUCTION)
public class UserConfig implements ApplicationRunner {

	@Autowired
	private UserService userService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.userService.init();
	}

}
