package springbook.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class DynamicProxyText {
	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("jsjg73"), is("Hello jsjg73"));
		assertThat(hello.sayHi("jsjg73"), is("Hi jsjg73"));
		assertThat(hello.sayThankYou("jsjg73"), is("Thank You jsjg73"));
		
		Hello proxyHello = new HelloUpperCase(new HelloTarget());
		assertThat(proxyHello.sayHello("jsjg73"), is("HELLO JSJG73"));
		assertThat(proxyHello.sayHi("jsjg73"), is("HI JSJG73"));
		assertThat(proxyHello.sayThankYou("jsjg73"), is("THANK YOU JSJG73"));
		
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] {Hello.class}, 
				new UppercaseHandler(new HelloTarget()));
		assertThat(proxiedHello.sayHello("jsjg73"), is("HELLO JSJG73"));
		assertThat(proxiedHello.sayHi("jsjg73"), is("HI JSJG73"));
		assertThat(proxiedHello.sayThankYou("jsjg73"), is("THANK YOU JSJG73"));
	}
	
	@Test
	public void proxyFactoryBean() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		assertThat(proxiedHello.sayHello("jsjg73"), is("HELLO JSJG73"));
		assertThat(proxiedHello.sayHi("jsjg73"), is("HI JSJG73"));
		assertThat(proxiedHello.sayThankYou("jsjg73"), is("THANK YOU JSJG73"));
		
	}
	
	static class UppercaseAdvice implements MethodInterceptor{

		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();
			return ret.toUpperCase();
		}
		
	}
	
	@Test
	public void pointcutAdvisor() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		assertThat(proxiedHello.sayHello("jsjg73"), is("HELLO JSJG73"));
		assertThat(proxiedHello.sayHi("jsjg73"), is("HI JSJG73"));
		assertThat(proxiedHello.sayThankYou("jsjg73"), is("Thank You jsjg73"));
	}
}
