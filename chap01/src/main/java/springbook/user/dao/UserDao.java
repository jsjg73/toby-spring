package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public abstract class UserDao {
	private DataSource dataSource; // 인터페이스를 통해 오브젝트에 접근하므로 구체적인 클래스 정보를 알 필요가 없다.
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource=dataSource;
	}
	
//	public UserDao(ConnectionMaker connectionMaker) {
//		AnnotationConfigApplicationContext context =
//				new AnnotationConfigApplicationContext(DaoFactory.class);
//		this.connectionMaker=context.getBean("connectionMaker", ConnectionMaker.class);
//	}
	
	/*
	1.7.3 의존관계 검색과 주입
	리스트 1-26 DaoFactory를 이용하는 생성자
	public UserDao(){
		DaoFacotry daoFactory = new DaoFactory();
		this.connectionMaker = daoFactory.connectionMaker();
	}
	 */

	/*
	1.7.3 의존관계 검색과 주입
	리스트 1-27 의존관계 검색을 이용하는 UserDao생성자
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
		User user =null;
		if(rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		if(user == null)throw new EmptyResultDataAccessException(1);
		return user;
	}
	
	public void deleteAll() throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c= dataSource.getConnection();
			
			ps = makeStatement(c);
			
			ps.executeUpdate();
		}catch (SQLException e) {
			throw e;
		}finally {
			if(ps!=null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if(c!=null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
		
	}
	
	abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;
//	{
//		PreparedStatement ps ;
//		ps = c.prepareStatement("delete from users");
//		return ps;
//	}

	public int getCount() throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(
					"select count(*) from users");
			
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
			
		}catch (SQLException e) {
			throw e;
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(ps!=null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if(c!=null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
		
	}
}
