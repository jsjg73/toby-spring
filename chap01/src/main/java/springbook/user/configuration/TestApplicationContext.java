package springbook.user.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mysql.jdbc.Driver;

@Configuration
@ImportResource("/test-applicationContext.xml")
public class TestApplicationContext {
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/spring_ex?characterEncoding=UTF-8");
		dataSource.setUsername("jsjg73");
		dataSource.setPassword("jsjg73");
		
		return dataSource;
	}
}
