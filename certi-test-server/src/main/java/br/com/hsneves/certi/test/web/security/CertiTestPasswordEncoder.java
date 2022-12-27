package br.com.hsneves.certi.test.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 
 * @author Henrique Neves
 *
 */
public class CertiTestPasswordEncoder implements PasswordEncoder {

	private static final Logger logger = LoggerFactory.getLogger(CertiTestPasswordEncoder.class);

	@Override
	public String encode(CharSequence rawPassword) {

		if (rawPassword == null) {
			throw new IllegalArgumentException("rawPassword cannot be null");
		}

		return CryptoUtils.encrypt(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {

		if (rawPassword == null) {
			throw new IllegalArgumentException("rawPassword cannot be null");
		}
		if (encodedPassword == null || encodedPassword.length() == 0) {
			logger.warn("Empty encoded password");
			return false;
		}

		return rawPassword.toString().equals(CryptoUtils.decrypt(encodedPassword));
	}

}