package springbook.user.dao;

public class DaoFactory {
	public UserDao userDao() {
		ConnectionMaker cm = new DConnectionMaker();
		UserDao dao = new UserDao(cm);
		return dao;
	}
}
