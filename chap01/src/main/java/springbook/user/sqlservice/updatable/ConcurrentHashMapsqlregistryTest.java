package springbook.user.sqlservice.updatable;

import springbook.issutracker.sqlservice.AbstractUpdatableSqlRegistryTest;
import springbook.issutracker.sqlservice.UpdatableSqlRegistry;

public class ConcurrentHashMapsqlregistryTest extends AbstractUpdatableSqlRegistryTest{

	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		return new ConcurrentHashMapSqlRegistry();
	}
}
