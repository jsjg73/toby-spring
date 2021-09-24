package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	UserDao dao;
	List<User> users;
	@Test
	public void bean() {
		assertThat(userService, notNullValue());
	}
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("Gabe", "Gabriel", "p1", Level.BASIC, 49, 0),
				new User("Harry", "Harold", "p2", Level.BASIC, 50, 0),
				new User("Minnie", "Hermione", "p3", Level.SILVER, 60, 29),
				new User("Ron", "Ronald", "p4", Level.SILVER, 60, 30),
				new User("Half-Blood", "snape", "p5", Level.GOLD, 100,100)
				);
	}
	
	@Test
	public void upgradeLevels() {
		dao.deleteAll();
		
		for(User user : users)dao.add(user);
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER );
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	}
	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = dao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
}
