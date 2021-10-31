package springbook.user.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import springbook.issutracker.sqlservice.EmbeddedDbSqlRegistry;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;

@Configuration
public class SqlServiceContext {
	/***
	 * SQL 서비스 
	 */
	@Bean
	public SqlService sqlService() {
		OxmSqlService oss = new OxmSqlService();
		oss.setUnmarshaller(unmarshaller());
		oss.setSqlRegistry(sqlRegistry());

		return oss;
	}
	
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}
	
	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
						.setName("embeddedDatabase")
						.setType(EmbeddedDatabaseType.HSQL)
						.addScript(
								"classpath:/springbook/user/sqlservice/updatable/sqlRegistrySchema.sql"
								)
						.build();
	}
	
	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("springbook.user.sqlservice.jaxb");
		return marshaller;
	}
}
