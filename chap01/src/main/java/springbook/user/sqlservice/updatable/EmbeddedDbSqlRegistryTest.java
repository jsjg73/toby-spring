package springbook.user.sqlservice.updatable;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.support.TransactionTemplate;

import springbook.issutracker.sqlservice.AbstractUpdatableSqlRegistryTest;
import springbook.issutracker.sqlservice.EmbeddedDbSqlRegistry;
import springbook.issutracker.sqlservice.SqlUpdateFailureException;
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
	
	@Test
	public void transactionalUpdate() {
		checkFindResult("SQL1","SQL2", "SQL3");
		
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified");
		sqlmap.put("KEY99999", "Modified99999");
		
		try {
			sqlRegistry.updateSql(sqlmap);
			fail();
		}catch(SqlUpdateFailureException e) {}
		
		checkFindResult("SQL1","SQL2", "SQL3");
	}
	@After
	public void tearDown() {
		db.shutdown();
	}
}
