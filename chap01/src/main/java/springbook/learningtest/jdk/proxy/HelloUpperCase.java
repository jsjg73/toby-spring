package springbook.learningtest.jdk.proxy;

public class HelloUpperCase implements Hello {
	Hello hello;

	public HelloUpperCase(Hello hello) {
		super();
		this.hello = hello;
	}

	public String sayHello(String name) {
		return hello.sayHello(name).toUpperCase();
	}


	public String sayHi(String name) {
		return hello.sayHi(name).toUpperCase();
	}

	public String sayThankYou(String name) {
		return hello.sayThankYou(name).toUpperCase();
	}

}
