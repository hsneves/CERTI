package br.com.hsneves.certi.test.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.hsneves.certi.test.entity.BaseEntity;
import br.com.hsneves.certi.test.entity.User;
import br.com.hsneves.certi.test.service.BaseService;
import br.com.hsneves.certi.test.web.security.CertiAuthenticationFacade;

/**
 * 
 * @author Henrique Neves
 *
 */
public abstract class BaseControllerImpl<S extends BaseService<T, ID>, T extends BaseEntity, ID> implements BaseController<S, T, ID> {

	@Autowired
	private CertiAuthenticationFacade authenticationFacade;

	/**
	 * 
	 */
	public User getLoggedUser() {
		return this.authenticationFacade.getUser();
	}

	@Override
	public List<T> all() {
		return getService().findAll();
	}

	@Override
	public T add(@Valid @RequestBody T obj) {
		return getService().add(obj);
	}

	@Override
	public T create() {
		return getService().newInstance();
	}

	@Override
	public T get(@PathVariable ID id) {
		// TODO: recuperar entidade do parâmetro genérico
		return getService().findById(id).orElseThrow(() -> new br.com.hsneves.certi.test.exceptions.EntityNotFoundException(BaseEntity.class, (Long) id));
	}

	@Override
	public T update(@Valid @RequestBody T obj, @PathVariable ID id) {
		return getService().update(id, obj);
	}

	@Override
	public void delete(@PathVariable ID id) {
		getService().deleteById(id);
	}
}
