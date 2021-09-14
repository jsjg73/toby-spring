package springbook.learningtest.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.either;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class JUnitTest {
	@Autowired
	ApplicationContext context;
	
	@Autowired
	UserDao dao;
	
	static UserDao userDao;
	static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
	static ApplicationContext contextObject = null;
	@Test
	public void test1() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject==null || contextObject==this.context, is(true));
		contextObject = this.context;
	}
	@Test
	public void test2() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertTrue(contextObject==null || contextObject==this.context);
		contextObject = this.context;
	}
	@Test
	public void test3() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject, 
				either(is(nullValue())).or(is(this.context)));
		contextObject = this.context;
	}
	
	@Test
	public void singletonTest1() {
		assertTrue(dao!=null);
		assertThat(userDao, either(nullValue()).or(is(dao)));
		userDao = dao;
	}
	
	@Test
	public void singletonTest2() {
		assertTrue(dao!=null);
		assertThat(userDao, either(nullValue()).or(is(dao)));
		userDao = dao;
	}
	
	@Test(expected = NoSuchBeanDefinitionException.class)
	public void wrongBeanName() {
		context.getBean("unkown", UserDao.class);
	}
	
	@Test
	public void DIAndDL() {
		assertThat(dao, sameInstance(context.getBean("userDao", UserDao.class)));
	}
	
	@Test
	public void xmlPropertyDI() {
		User user = context.getBean("user", User.class);
		assertThat(user, notNullValue());
		assertThat(user.getId(), equalTo("jsjg73"));
	}
	@Test
	public void xmlPropertyDI_Fail() {
		User user = context.getBean("user", User.class);
		assertThat(user, notNullValue());
		assertThat(user.getId(), not(equalTo("jsjg7")));
	}
}
