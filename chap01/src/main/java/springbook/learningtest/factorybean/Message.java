package springbook.learningtest.factorybean;

public class Message {
	String text;
	
	//생성자를 통해서 오브젝트를 만들 수 없도록 접근제어자 설정
	private Message(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public static Message newMessage(String text) {
		return new Message(text);
	}
}
