package shared.baas.keyvalue.firebase;

import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.DataObjectQuery;

import com.firebase.client.Firebase;

public class DataObjectFactory_ extends DataObjectFactory {
	Firebase root;
	
	public DataObjectFactory_(Firebase root) {
		this.root = root;
	}

	@Override
	public DataObject createDataObject(String className) {
		return new DataObject_(root, className);
	}

	@Override
	public DataObjectQuery createDataObjectQuery(String className) {
		return new DataObjectQuery_(this, className);
	}
}