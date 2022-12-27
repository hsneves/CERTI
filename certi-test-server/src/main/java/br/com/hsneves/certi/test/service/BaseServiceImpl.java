package br.com.hsneves.certi.test.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import br.com.hsneves.certi.test.entity.BaseEntity;
import br.com.hsneves.certi.test.repository.BaseRepository;

/**
 * 
 * @author Henrique Neves
 *
 */
public abstract class BaseServiceImpl<R extends BaseRepository<T, Long>, T extends BaseEntity, ID> implements BaseService<T, Long> {

	private static final String DEFAULT_ORDER_BY_COLUMN = "id";

	private R repository;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	protected BaseServiceImpl(R repository) {
		super();
		this.repository = repository;
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@Override
	public List<T> findAll() {
		return this.repository.findAll();
	}

	/**
	 * 
	 * @param search
	 * @return
	 */
	protected Example<T> getExample(String search) {
		return null;
	}

	/**
	 * 
	 * @param column
	 * @return
	 */
	protected String getOrderColumn(int column) {
		return DEFAULT_ORDER_BY_COLUMN;
	}

	@Override
	public List<T> findAll(Sort sort) {
		return this.repository.findAll(sort);
	}

	@Override
	public Optional<T> findById(Long id) {
		return this.repository.findById(id);
	}

	@Override
	public <S extends T> S save(S entity) {
		preSave(entity);
		entity = this.repository.save(entity);
		postSave(entity);
		return entity;
	}

	@Override
	public T newInstance() {
		try {
			T instance = this.entityClass.newInstance();
			newEntityPropertiesUpdate(instance);
			return instance;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public T add(T entity) {
		return save(entity);
	}

	@Override
	public T update(Long id, T newEntity) {
		return findById(id).map((entity) -> {
			entityPropertiesUpdate(entity, newEntity);
			return save(entity);
		}).orElseGet(() -> {
			newEntity.setId(id);
			return save(newEntity);
		});
	}

	/**
	 * 
	 * @param entity
	 */
	protected void preAdd(T entity) {

	}

	/**
	 * 
	 * @param entity
	 */
	protected void postAdd(T entity) {

	}

	/**
	 * 
	 * @param entity
	 */
	protected void preUpdate(T entity) {

	}

	/**
	 * 
	 * @param entity
	 */
	protected void postUpdate(T entity) {

	}

	/**
	 * 
	 * @param entity
	 */
	protected void preSave(T entity) {

	}

	/**
	 * 
	 * @param entity
	 */
	protected void postSave(T entity) {

	}

	/**
	 * 
	 * @param entity
	 */
	protected void postDelete(Long id) {

	}

	/**
	 * 
	 * @param entity
	 */
	protected void newEntityPropertiesUpdate(T entity) {

	}

	/**
	 * 
	 * @param entity
	 * @param newEntity
	 */
	protected abstract void entityPropertiesUpdate(T entity, T newEntity);

	@Override
	public void deleteById(Long id) {
		validateRemove(id);
		this.repository.deleteById(id);
		postDelete(id);
	}

	/**
	 * 
	 * @param id
	 */
	protected void validateRemove(Long id) {

	}

	/**
	 * 
	 * @return
	 */
	public R getRepository() {
		return repository;
	}
}
