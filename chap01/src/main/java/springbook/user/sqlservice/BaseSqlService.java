package springbook.user.sqlservice;

import javax.annotation.PostConstruct;

public class BaseSqlService implements SqlService {
	private SqlRegistry sqlRegistry;
	private SqlReader sqlReader;
	
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}
	
	public SqlRegistry getSqlRegistry() {
		return sqlRegistry;
	}

	public SqlReader getSqlReader() {
		return sqlReader;
	}

	@PostConstruct
	public void loadSql() {
		System.out.println("baseSqlservice postconstruct");
		this.sqlReader.read(this.sqlRegistry);
	}
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return this.sqlRegistry.findsql(key);
		}catch(SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
	}

}
