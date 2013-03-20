package shared.baas.keyvalue;

import shared.baas.DataClassFacade;
import shared.baas.impl.BasicDataClassFacade;



public abstract class DataObjectFactory {
	public abstract DataObject createDataObject(String className);
	public abstract DataObjectQuery createDataObjectQuery(String className);
	
	public <T> DataClassFacade<T> get(Class<T> clazz) {
		return new BasicDataClassFacade<T>(this, clazz);
	}
}