package br.com.hsneves.certi.test.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.hsneves.certi.test.entity.BaseEntity;
import br.com.hsneves.certi.test.entity.User;
import br.com.hsneves.certi.test.service.BaseService;

/**
 * 
 * @author Henrique Neves
 *
 */
interface BaseController<S extends BaseService<T, ID>, T extends BaseEntity, ID> {

	/**
	 * 
	 * @return
	 */
	User getLoggedUser();
	
	/**
	 * 
	 * @return
	 */
	S getService();

	/**
	 * 
	 * @return
	 */
	List<T> all();

	/**
	 * 
	 * @param obj
	 * @return
	 */
	T add(@Valid @RequestBody T obj);

	/**
	 * 
	 * @return
	 */
	T create();
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	T get(@PathVariable ID id);

	/**
	 * 
	 * @param obj
	 * @param id
	 * @return
	 */
	T update(@Valid @RequestBody T obj, @PathVariable ID id);

	/**
	 * 
	 * @param id
	 */
	void delete(@PathVariable ID id);
	
}
