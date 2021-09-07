package springbook.test;

import java.sql.SQLException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public class UserDaoTest {
	
	@Test
	public void addAndGet() throws SQLException{
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("jsjg73");
		user.setName("재성");
		user.setPassword("123");
		
		dao.add(user);
		
		System.out.println(user.getId()+" 등록 성공");
		
		User user2 = new User();
		user2=dao.get("jsjg73");
		if(!user.getName().equals(user2.getName())){
			System.out.println("테스트 실패 (name)");
		}else if(!user.getPassword().equals(user2.getPassword())) {
			System.out.println("테스트 실패 (password)");
		}else {
			System.out.println("조회 테스트 성공");
		}
	}
	
}
