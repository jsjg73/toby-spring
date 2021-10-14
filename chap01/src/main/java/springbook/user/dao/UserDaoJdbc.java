package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao {
	
	private Map<String, String> sqlMap;
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	// 리스트 3-56 재사용 가능하도록 독립시킨 RowMapper
	private RowMapper<User> userMapper = new RowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmail(rs.getString("email"));
			return user;
		}
	};
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void add(final User user) {
		//""
		this.jdbcTemplate.update(this.sqlMap.get("add"), 
				user.getId(),user.getName(),user.getPassword(), 
				user.getLevel().intValue(), user.getLogin(),
				user.getRecommend(),user.getEmail());
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject(
				sqlMap.get("get"),
				new Object[] {id}, this.userMapper);
	}

	public void deleteAll() {
		this.jdbcTemplate.update(sqlMap.get("deleteAll"));
	}

	public int getCount() {
		return this.jdbcTemplate.queryForInt(sqlMap.get("getCount"));
	}
	public List<User> getAll(){
		return this.jdbcTemplate.query(sqlMap.get("getAll"), this.userMapper);
	}

	public void update(User user) {
		this.jdbcTemplate.update(
				sqlMap.get("update"),
				user.getName(), user.getPassword(), user.getLevel().intValue(), 
				user.getLogin(), user.getRecommend(),user.getEmail(),user.getId()
				);
		
	}
}
