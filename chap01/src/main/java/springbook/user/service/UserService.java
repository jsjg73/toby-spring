package springbook.user.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import springbook.user.domain.User;

@Transactional
public interface UserService {

	public void upgradeLevels();
	public void deleteAll() ;
	public void update(User user);
	public void add(User user);
	
	@Transactional(readOnly = true)
	public User get(String id) ;
	
	@Transactional(readOnly = true)
	public List<User> getAll();

}