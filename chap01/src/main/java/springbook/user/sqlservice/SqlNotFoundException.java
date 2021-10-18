package springbook.user.sqlservice;

public class SqlNotFoundException extends RuntimeException {
	public SqlNotFoundException (String msg){
		super(msg);
	}
	public SqlNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}
}
