package shared.baas.keyvalue.stackmob;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.DataObjectQuery;

public class DataObjectFactorySM extends DataObjectFactory {
	@Override
	public DataObject createDataObject(String className) {
		return new DataObjectSM(className);
	}

	@Override
	public DataObjectQuery createDataObjectQuery(String className) {
		return new DataObjectQuerySM(className);
	}
	
}