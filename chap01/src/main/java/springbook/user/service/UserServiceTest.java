package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/t-applicationContext.xml")
public class UserServiceTest {
	@Autowired UserService userService;
	@Autowired UserDao dao;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired MailSender dummyMailSender; 
	@Autowired UserServiceImpl userServiceImpl;
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
	@DirtiesContext // 컨텍스트의 DI설정을 변경하는 테스트라는 것을 명시.
	public void upgradeLevels() throws Exception{
		dao.deleteAll();
		for(User user : users)dao.add(user);
		
		MockMailSender mockMailSender = new MockMailSender(); 
		userServiceImpl.setMailSender(mockMailSender); // userService의 의존 오프젝트로 주입
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), false);
		checkLevel(users.get(1), true);
		checkLevel(users.get(2), false);
		checkLevel(users.get(3), true);
		checkLevel(users.get(4), false);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
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
		UserServiceImpl test = new TestUserService (users.get(3).getId());
		test.setUserDao(this.dao);
		test.setMailSender(dummyMailSender);
		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(test);
		
		dao.deleteAll();
		for(User user: users)dao.add(user);
		
		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e) {}
		
		checkLevel(users.get(1), false);
	}
	static class TestUserService extends UserServiceImpl{
		private String id;
		public TestUserService(String id) {
			this.id= id;
		}
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}
	static class TestUserServiceException extends RuntimeException{}
	// 메일 전송 검증용 목 오브젝트
	// UserService의 코드가 정상적으로 수행되도록 돕는 역할이 우선
	// 테스트 대상이 넘겨주는 출력 값을 보관해두는 기능을 추가
	// 리스트5-57 목 오브젝트로 만든 메일 전송 확인용 클래스
	static class MockMailSender implements MailSender{
		private List<String> requests = new ArrayList<String>();

		public List<String> getRequests() {
			return requests;
		}

		public void send(SimpleMailMessage simpleMessage) throws MailException {
			requests.add(simpleMessage.getTo()[0]);
		}

		public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		}
		
	}
}
