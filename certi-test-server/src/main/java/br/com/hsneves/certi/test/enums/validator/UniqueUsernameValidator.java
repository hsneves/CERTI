package br.com.hsneves.certi.test.enums.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.hsneves.certi.test.entity.User;
import br.com.hsneves.certi.test.service.UserService;

/**
 * 
 * @author Henrique Neves
 *
 */
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, User> {

	@Autowired
	private UserService userService;

	@Override
	public boolean isValid(User user, ConstraintValidatorContext context) {

		if (user == null) {
			return true;
		}

		if (!(user instanceof User)) {
			throw new IllegalArgumentException("@UniqueUsername only applies to " + User.class.getName());
		}

		if (this.userService != null) {
			//return !this.userService.isUsernameInUse(user.getId(), user.getUsername());
		}

		return true;
	}

}