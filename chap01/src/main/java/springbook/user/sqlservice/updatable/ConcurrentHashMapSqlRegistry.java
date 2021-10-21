package springbook.user.sqlservice.updatable;

import java.util.Map;

import springbook.issutracker.sqlservice.SqlUpdateFailureException;
import springbook.issutracker.sqlservice.UpdatableSqlRegistry;
import springbook.user.sqlservice.SqlNotFoundException;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

	@Override
	public void registerSql(String key, String sql) {
		// TODO Auto-generated method stub

	}

	@Override
	public String findsql(String key) throws SqlNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
		// TODO Auto-generated method stub

	}

}
