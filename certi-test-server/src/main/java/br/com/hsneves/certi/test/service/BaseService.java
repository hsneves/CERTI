package br.com.hsneves.certi.test.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import br.com.hsneves.certi.test.entity.BaseEntity;

/**
 * 
 * @author Henrique Neves
 *
 */
public interface BaseService<T extends BaseEntity, ID> {

	/**
	 * 
	 * @return
	 */
	List<T> findAll();

	/**
	 * 
	 * @param sort
	 * @return
	 */
	List<T> findAll(Sort sort);

	/**
	 * 
	 * @param id
	 * @return
	 */
	Optional<T> findById(ID id);

	/**
	 * 
	 * @param <S>
	 * @param entity
	 * @return
	 */
	<S extends T> S save(S entity);

	/**
	 * 
	 * @return
	 */
	T newInstance();

	/**
	 * 
	 * @param entity
	 * @return
	 */
	T add(T entity);

	/**
	 * 
	 * @param id
	 * @param entity
	 * @return
	 */
	T update(ID id, T entity);

	/**
	 * 
	 * @param id
	 */
	void deleteById(ID id);

}