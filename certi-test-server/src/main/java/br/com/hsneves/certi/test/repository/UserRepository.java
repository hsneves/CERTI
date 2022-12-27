package br.com.hsneves.certi.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.hsneves.certi.test.entity.User;

/**
 * 
 * @author Henrique Neves
 *
 */
@Repository
public interface UserRepository extends BaseRepository<br.com.hsneves.certi.test.entity.User, Long> {

	/**
	 * Encontrar {@link User} pelo username.
	 * 
	 * @param username Username
	 * @return
	 */
	@Query("from User where username = ?1")
	Optional<User> findByUsername(String username);

	
}
