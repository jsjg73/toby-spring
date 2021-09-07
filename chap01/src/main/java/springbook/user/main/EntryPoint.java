package springbook.user.main;

import java.sql.SQLException;

import org.junit.runner.JUnitCore;

/*리스트 1-3 테스트용 main() 메소드*/
public class EntryPoint {
	public static void main(String[] args) throws SQLException {
		JUnitCore.main("springbook.test.UserDaoTest");
	}
}
