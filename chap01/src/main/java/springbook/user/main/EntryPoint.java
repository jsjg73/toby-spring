package springbook.user.main;

import java.sql.SQLException;

import org.junit.runner.JUnitCore;

/*����Ʈ 1-3 �׽�Ʈ�� main() �޼ҵ�*/
public class EntryPoint {
	public static void main(String[] args) throws SQLException {
		JUnitCore.main("springbook.test.UserDaoTest");
	}
}
