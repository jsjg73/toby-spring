package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/t-applicationContext.xml")
public class UserServiceTest {
	@Autowired ApplicationContext context;
	@Autowired UserService userService;
	@Autowired UserService testUserService;
	@Autowired UserDao dao;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired MailSender dummyMailSender; 
	List<User> users;
	
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
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MockMailSender mockMailSender = new MockMailSender(); 
		userServiceImpl.setMailSender(mockMailSender); // userService의 의존 오프젝트로 주입
		
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "Harry", Level.SILVER);
		checkUserAndLevel(updated.get(1), "Ron", Level.GOLD);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
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
		dao.deleteAll();
		for(User user: users)dao.add(user);
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e) {}
		
		checkLevel(users.get(1), false);
	}
	static class TestUserService extends UserServiceImpl{
		private String id ="Ron";
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
		@Override
		public List<User> getAll() {
			for(User user : super.getAll()) {
				super.update(user);
			}
			return null;
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
	
	static class MockUserDao implements UserDao{
		private List<User> users;
		private List<User> updated=new ArrayList<User>();
		
		public MockUserDao(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated() {
			return updated;
		}

		public List<User> getAll() {
			return this.users;
		}


		public void update(User user) {
			updated.add(user);
		}
		
		public void add(User user) {throw new UnsupportedOperationException();}
		public User get(String id) {throw new UnsupportedOperationException();}
		public void deleteAll() {throw new UnsupportedOperationException();}
		public int getCount() {throw new UnsupportedOperationException();}
	}
	
	@Test
	public void mockUpgradeLevels() throws Exception{
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = 
				ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender,times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
	}
	
	@Test(expected = TransientDataAccessResourceException.class)
	public void readOnlyTransactionAtrribute() {
		testUserService.getAll();
	}
	
	@Test
	public void transactionSync() {
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
		userService.deleteAll();
		
		userService.add(users.get(0));
		userService.add(users.get(1));
		
		transactionManager.commit(txStatus);
	}
}
