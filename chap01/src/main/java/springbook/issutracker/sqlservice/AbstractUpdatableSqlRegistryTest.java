package springbook.issutracker.sqlservice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import springbook.issutracker.sqlservice.SqlUpdateFailureException;
import springbook.issutracker.sqlservice.UpdatableSqlRegistry;
import springbook.user.sqlservice.SqlNotFoundException;

public abstract class AbstractUpdatableSqlRegistryTest {
	UpdatableSqlRegistry sqlRegistry;

	@Before
	public void setUp() {
		sqlRegistry = createUpdatableSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");

	}

	abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

	@Test
	public void find() {
		checkFindResult("SQL1", "SQL2", "SQL3");
	}

	protected void checkFindResult(String expected1, String expected2, String expected3) {
		assertThat(sqlRegistry.findsql("KEY1"), is(expected1));
		assertThat(sqlRegistry.findsql("KEY2"), is(expected2));
		assertThat(sqlRegistry.findsql("KEY3"), is(expected3));
	}

	@Test(expected = SqlNotFoundException.class)
	public void unkownKey() {
		sqlRegistry.findsql("SLQ999!$!");
	}

	@Test
	public void updateSingle() {
		sqlRegistry.updateSql("KEY2", "Modified2");
		checkFindResult("SQL1", "Modified2", "SQL3");
	}

	@Test
	public void updateMulti() {
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modi1");
		sqlmap.put("KEY3", "Modi3");

		sqlRegistry.updateSql(sqlmap);
		checkFindResult("Modi1", "SQL2", "Modi3");
	}

	@Test(expected = SqlUpdateFailureException.class)
	public void updateWithNotExistingKey() {
		sqlRegistry.updateSql("SLQ9999", "Modi2");
	}
}
