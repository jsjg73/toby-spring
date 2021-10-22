package springbook.user.sqlservice.updatable;

import org.junit.After;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import springbook.issutracker.sqlservice.AbstractUpdatableSqlRegistryTest;
import springbook.issutracker.sqlservice.EmbeddedDbSqlRegistry;
import springbook.issutracker.sqlservice.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
	EmbeddedDatabase db;
	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.addScript("classpath:/springbook/user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
		EmbeddedDbSqlRegistry registry = new EmbeddedDbSqlRegistry();
		registry.setDataSource(db);
		return registry;
	}

	@After
	public void tearDown() {
		db.shutdown();
	}
}
