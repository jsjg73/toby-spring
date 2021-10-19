package springbook.user.sqlservice;


public class DefaultSqlService extends BaseSqlService{
//	public DefaultSqlService() {
//		System.out.println("DefaultSqlService constructor");
//		if(getSqlReader() == null) {
//			setSqlReader(new JaxbXmlSqlReader());
//		}
//		if(getSqlRegistry()==null) {
//			setSqlRegistry(new HashMapSqlRegistry());
//		}
//	}
	
	@Override
	public void loadSql() {
		System.out.println("DefaultSqlService postconstructor");
		if(getSqlReader() == null) {
			setSqlReader(new JaxbXmlSqlReader());
		}
		if(getSqlRegistry()==null) {
			setSqlRegistry(new HashMapSqlRegistry());
		}
		super.loadSql();
	}
}
