package springbook.issutracker.sqlservice;

public class SqlUpdateFailureException extends RuntimeException {
	public SqlUpdateFailureException() {
	}
	public SqlUpdateFailureException(String msg) {
		super(msg);
	}
	public SqlUpdateFailureException(Throwable e) {
		super(e);
	}
	public SqlUpdateFailureException(String msg, Throwable e) {
		super(msg,e);
	}
}
