package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {
	
	@Test
	public void addAndGet() throws SQLException{
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		User user = new User();
		user.setId("jsjg73");
		user.setName("Àç¼º");
		user.setPassword("123");
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));
		
		User user2 = dao.get("jsjg73");
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));

	}
	
}
