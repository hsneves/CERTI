package br.com.hsneves.certi.test.exceptions;

import br.com.hsneves.certi.test.entity.BaseEntity;

/**
 * 
 * @author Henrique Neves
 *
 */
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 156887350393653224L;

	private Class<? extends BaseEntity> entityClass;

	private Long id;

	public EntityNotFoundException(Class<? extends BaseEntity> entityClass, Long id) {
		super();
		this.entityClass = entityClass;
		this.id = id;
	}

	@Override
	public String getMessage() {
		return "Entity not found: entity = [" + this.entityClass.getSimpleName() + "], id = [" + id + "]";
	}

	public Class<? extends BaseEntity> getEntityClass() {
		return entityClass;
	}

	public Long getId() {
		return id;
	}
}