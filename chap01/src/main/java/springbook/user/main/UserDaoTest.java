package springbook.user.main;

import java.sql.SQLException;

import springbook.user.dao.ConnectionMaker;
import springbook.user.dao.DConnectionMaker;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/*����Ʈ 1-3 �׽�Ʈ�� main() �޼ҵ�*/
public class UserDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ConnectionMaker cm = new DConnectionMaker();
		UserDao dao = new UserDao(cm);
		
		User user = new User();
		user.setId("jsjg73");
		user.setName("�缺");
		user.setPassword("123");
		
		dao.add(user);
		
		System.out.println(user.getId()+" ��� ����");
		
		User user2 = new User();
		user2=dao.get("jsjg73");
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId()+" ��ȸ ����");
	}
}
