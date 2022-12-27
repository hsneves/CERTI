package br.com.hsneves.certi.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.hsneves.certi.test.entity.BaseEntity;

/**
 * 
 * Super classe para os repositórios do projeto
 * 
 * @author Henrique Neves
 *
 * @param <E>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

}
