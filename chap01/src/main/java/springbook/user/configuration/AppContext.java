package springbook.user.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.Driver;

import springbook.issutracker.sqlservice.EmbeddedDbSqlRegistry;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.service.UserServiceTest.TestUserService;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "springbook.user")
public class AppContext {
	@Autowired UserDao userDao;
	
	/*
	 * DB 연결과 트랜잭션
	 * */
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/spring_ex?characterEncoding=UTF-8");
		dataSource.setUsername("jsjg73");
		dataSource.setPassword("jsjg73");

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		return tm;
	}
	
	/*
	 * 애플리케이션 로직 & 테스트
	 * */
	
	@Bean
	public UserService testUserService() {
		TestUserService testService = new TestUserService();
		testService.setUserDao(this.userDao);
		testService.setMailSender(mailSender());
		return testService; 
	}
	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
	
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
