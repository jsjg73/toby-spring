package springbook.user.sqlservice;

public interface SqlRegistry {
	void registerSql(String key, String sql);
	
	String findsql(String key)throws SqlNotFoundException;
}
