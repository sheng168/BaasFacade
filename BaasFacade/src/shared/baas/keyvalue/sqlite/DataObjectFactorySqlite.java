package shared.baas.keyvalue.sqlite;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.DataObjectQuery;

public class DataObjectFactorySqlite implements DataObjectFactory {
	@Override
	public DataObject createDataObject(String className) {
		return new DataObjectSqlite(className);
	}

	@Override
	public DataObjectQuery createDataObjectQuery(String className) {
		return new DataObjectQuerySqlite(className);
	}
	
}