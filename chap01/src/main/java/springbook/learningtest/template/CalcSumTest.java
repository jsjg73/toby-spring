package springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {
	private Calculator calculator;
	private String numFilePath;
	@Before
	public void setUp() {
		this.calculator=new Calculator();
		this.numFilePath=getClass().getResource("numbers.txt").getPath();
	}
	@Test
	public void sumOfNumbers() throws IOException {
		int sum = calculator.calcSum(this.numFilePath);
		assertThat(sum, is(10));
	}
	
	@Test
	public void multiplyOfNumbers()throws IOException{
		assertThat(calculator.calcMultiply(this.numFilePath), is(24));
	}
	@Test
	public void concatenateStrings()throws IOException{
		assertThat(calculator.concatenate(numFilePath), is("1234"));
	}
}
