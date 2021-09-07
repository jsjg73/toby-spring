package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDao {
	private DataSource dataSource; // �������̽��� ���� ������Ʈ�� �����ϹǷ� ��ü���� Ŭ���� ������ �� �ʿ䰡 ����.
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource=dataSource;
	}
	
//	public UserDao(ConnectionMaker connectionMaker) {
//		AnnotationConfigApplicationContext context =
//				new AnnotationConfigApplicationContext(DaoFactory.class);
//		this.connectionMaker=context.getBean("connectionMaker", ConnectionMaker.class);
//	}
	
	/*
	1.7.3 �������� �˻��� ����
	����Ʈ 1-26 DaoFactory�� �̿��ϴ� ������
	public UserDao(){
		DaoFacotry daoFactory = new DaoFactory();
		this.connectionMaker = daoFactory.connectionMaker();
	}
	 */

	/*
	1.7.3 �������� �˻��� ����
	����Ʈ 1-27 �������� �˻��� �̿��ϴ� UserDao������
	public UserDao(){
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(DaoFactory.class);
		this.connectionMaker=context.getBean("connectionMaker", ConnectionMaker.class);
	}
	 */
	
	
	public void add(User user) throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"insert into users(id, name, password) values(?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		ps.close();
		c.close();
	}
	public User get(String id)throws SQLException{
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"select * from users where id = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
	}
	
	public void delete() throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("delete from users");
		ps.executeUpdate();
		ps.close();
		c.close();
	}
}
