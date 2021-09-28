package springbook.user.domain;

public class User {
	String id;
	String name;
	String password;
	Level level;
	int login;
	int recommend;
	String email;
	
	public User(String id, String name, String password, Level level, int login, int recommend, String email) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.level = level;
		this.login = login;
		this.recommend = recommend;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	public User() {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void upgradeLevel() {
		if(level==null)
			throw new IllegalStateException("현재 Level이 null이면 업그레이드가 불가능합니다");
			
		Level nextLevel = level.nextLevel();
		if(nextLevel==null) {
			throw new IllegalStateException(this.level+"은 업그레이드가 불가능합니다");
		}
		else {
			this.level=nextLevel;
		}
	}
}
