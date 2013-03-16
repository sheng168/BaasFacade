package shared.baas.keyvalue.parse;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.DataObjectQuery;

import com.parse.ParseObject;

public class DataObjectFactoryParse implements DataObjectFactory {
	@Override
	public DataObject createDataObject(String className) {
		return new DataObjectParse(new ParseObject(className));
	}

	@Override
	public DataObjectQuery createDataObjectQuery(String className) {
		return new DataObjectQueryParse(className);
	}
	
}