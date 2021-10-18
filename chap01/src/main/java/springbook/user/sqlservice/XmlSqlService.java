package springbook.user.sqlservice;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService, SqlRegistry {
	Map<String, String> sqlMap = new HashMap<String, String>();
	private String sqlmapFile;
	private SqlRegistry sqlRegistry;
	private SqlReader sqlReader;
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	@PostConstruct
	public void loadSql() {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			
			JAXBContext context =  JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(UserDao.class.getResourceAsStream(this.sqlmapFile));
			
			for(SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		}catch(JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if(sql == null)
			throw new SqlRetrievalFailureException(key+
					"에 대한 SQL을 찾을 수 없습니다.");
		return sql;
	}

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String findsql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if(sql == null)
			throw new SqlNotFoundException(key+
					"에 대한 SQL을 찾을 수 없습니다.");
		return sql;
	}
}
