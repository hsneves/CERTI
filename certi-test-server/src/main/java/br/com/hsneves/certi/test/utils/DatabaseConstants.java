package br.com.hsneves.certi.test.utils;

/**
 * 
 * @author Henrique Neves
 *
 */
public class DatabaseConstants {

	/**
	 * Database/JPA Settings
	 */
	public static final String PERSISTENCE_UNIT = "certi-test-pu";

	public static final String BASE_ENTITY_PACKAGE = "br.com.hsneves.certi.test.entity.base";
	
	public static final String ENTITY_PACKAGE = "br.com.hsneves.certi.test.entity";

	public static final String REPOSITORY_PACKAGE = "br.com.hsneves.certi.test.repository";

	public static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";

	public static final String DATA_SOURCE = "dataSource";

	public static final String TRANSACTION_MANAGER = "transactionManager";

	public static final String[] PACKAGES_TO_SCAN = new String[] { BASE_ENTITY_PACKAGE, ENTITY_PACKAGE };

	public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

	public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";

	public static final String HIBERNATE_DIALECT = "hibernate.dialect";

	public static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
}
