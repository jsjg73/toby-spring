package springbook.user.service;

import java.util.List;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for(User user : users) {
			if(canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}
	private void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
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
}
