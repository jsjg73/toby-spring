package springbook.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HelloTest {
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
	}
}
