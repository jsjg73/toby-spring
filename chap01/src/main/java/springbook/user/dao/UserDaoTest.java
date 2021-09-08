package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest {
	@Autowired
	private ApplicationContext context;
	
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		dao = context.getBean("userDao", UserDao.class);
		user1 = new User("jsjg73", "재성", "123");
		user2 = new User("kjs9373", "김재성", "123");
		user3 = new User("real", "희동이", "345");
	}
	@Test
	public void addAndGet() throws SQLException{
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));

	}
	@Test
	public void count() throws SQLException{
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
		
		
	}
	
	// 리스트2-13 get() 메소드의 예외상황에 대한 테스트
	@Test(expected=EmptyResultDataAccessException.class) // 발생할 것으로 기대하는 예외 클래스
	public void getUserFailure() throws SQLException{
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		//여기서 EmptyResultDataAccessException 예와가 발생한다.
		//예외가 발생하지 않으면 테스트가 실패한다.
		dao.get("unknown_id");
		
	}
}
