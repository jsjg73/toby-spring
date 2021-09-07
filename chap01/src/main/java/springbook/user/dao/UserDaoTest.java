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
		dao.delete();
		
		User user = new User();
		user.setId("jsjg73");
		user.setName("재성");
		user.setPassword("123");
		dao.add(user);
		
		System.out.println(user.getId()+" 등록 성공");
		
		User user2 = new User();
		user2=dao.get("jsjg73");
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));

	}
	
}
