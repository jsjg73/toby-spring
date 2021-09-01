package springbook.user.main;

import java.sql.SQLException;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/*리스트 1-3 테스트용 main() 메소드*/
public class UserDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		UserDao dao = new DaoFactory().userDao();
		
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
	}
}
