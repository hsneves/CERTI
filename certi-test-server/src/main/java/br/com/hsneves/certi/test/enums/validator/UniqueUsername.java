package br.com.hsneves.certi.test.enums.validator;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * @author Henrique Neves
 *
 */
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RUNTIME)
@Target({ ElementType.TYPE })
public @interface UniqueUsername {

	String message() default "Já existe outro usuário cadastrado com o mesmo nome";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
}