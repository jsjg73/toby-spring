package springbook.user.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	UserDao userDao;
	private PlatformTransactionManager transactionManager;
	
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	//트랜잭션을 조정하고 싶은 영역.
	public void upgradeLevels() throws Exception {
		TransactionStatus status =
				transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			List<User> users = userDao.getAll();
			for(User user : users) {
				if(canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	// 상속을 통해 오버라이딩 가능하도록 변경
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}

	private void sendUpgradeEMail(User user) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.ksug.org");
		Session s = Session.getInstance(props, null);
		
		MimeMessage msg = new MimeMessage(s);
		try {
			msg.setFrom(new InternetAddress("useradmin@ksug.org"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			msg.setSubject("Upgrade 안내");
			msg.setText("사용자님의 등급이 "+ user.getLevel().name() + "로 업그레이드되었습니다.");
			
			Transport.send(msg);
		}catch (AddressException e) {
			throw new RuntimeException();
		} catch (MessagingException e) {
			throw new RuntimeException();
		}
	}

	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		
		switch(currentLevel) {
			case BASIC: return user.getLogin()>= MIN_LOGCOUNT_FOR_SILVER;
			case SILVER: return user.getRecommend()>= MIN_RECOMMEND_FOR_GOLD;
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level: "+currentLevel);//새로운 레벨에 대해 로직을 추가 하지 않으면 에러가 발생하므로 쉽게 확인할 수 있음!!
		}
	}

	public void add(User user) {
		if(user.getLevel()==null) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
	
	static class TestUserService extends UserService{
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
	
}
