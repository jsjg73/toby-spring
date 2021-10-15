package springbook.user.sqlservice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService {
	
	Map<String, String> sqlMap = new HashMap<String, String>();
	
	public XmlSqlService() {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			
			JAXBContext context =  JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(UserDao.class.getResourceAsStream("sqlmap.xml"));
			
			for(SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		}catch(JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if(sql == null)
			throw new SqlRetrievalFailureException(key+
					"에 대한 SQL을 찾을 수 없습니다.");
		return sql;
	}
}
