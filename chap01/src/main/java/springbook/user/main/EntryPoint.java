package springbook.user.main;

import java.sql.SQLException;

import org.junit.runner.JUnitCore;

public class EntryPoint {
	public static void main(String[] args) throws SQLException {
		JUnitCore.main("springbook.user.dao.UserDaoTest");
	}
}
