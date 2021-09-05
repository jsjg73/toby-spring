package springbook.user.main;

import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.CountingConnectionMaker;
import springbook.user.dao.CountingDaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public class UserDaoConnectionCountingTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDao dao =context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("jsjg73");
		user.setName("재성");
		user.setPassword("123");
		
		dao.add(user);
		
		System.out.println(user.getId()+" 등록 성공");
		
		User user2 = new User();
		user2=dao.get("jsjg73");
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId()+" 조회 성공");
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
		System.out.println("Connection counter : "+ccm.getCounter());
	}
}
