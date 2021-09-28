package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.UserService.TestUserServiceException;

import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	UserDao dao;
	@Autowired
	PlatformTransactionManager transactionManager;
	List<User> users;
	@Test
	public void bean() {
		assertThat(userService, notNullValue());
	}
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("Gabe", "Gabriel", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "mail1"),
				new User("Harry", "Harold", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "mail2"),
				new User("Minnie", "Hermione", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "mail3"),
				new User("Ron", "Ronald", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "mail4"),
				new User("Half-Blood", "snape", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "mail5")
				);
	}
	
	@Test
	public void upgradeLevels() throws Exception{
		dao.deleteAll();
		
		for(User user : users)dao.add(user);
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), false);
		checkLevel(users.get(1), true);
		checkLevel(users.get(2), false);
		checkLevel(users.get(3), true);
		checkLevel(users.get(4), false);
	}
	private void checkLevel(User user, boolean upgraded) {
		User userUpdate = dao.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	@Test
	public void add() {
		dao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = dao.get(userWithLevel.getId());
		User userWithoutLevelRead =dao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
	
	@Test
	public void upgradeAllOrNothing()throws Exception {
		UserService test = new UserService.TestUserService (users.get(3).getId());
		test.setUserDao(this.dao);
		test.setTransactionManager(transactionManager);
		dao.deleteAll();
		for(User user: users)dao.add(user);
		
		try {
			test.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e) {}
		
		checkLevel(users.get(1), false);
	}
}
