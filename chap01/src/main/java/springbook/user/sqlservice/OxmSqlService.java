package springbook.user.sqlservice;

import org.springframework.oxm.Unmarshaller;

public class OxmSqlService implements SqlService {
	private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class OxmSqlReader implements SqlReader{
		private Unmarshaller unmarshaller;
		
		public void setUnmarshaller(Unmarshaller unmarshaller) {
			this.unmarshaller = unmarshaller;
		}

		@Override
		public void read(SqlRegistry sqlRegistry) {
		}
	}
}
