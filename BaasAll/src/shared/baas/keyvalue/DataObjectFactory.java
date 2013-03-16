package shared.baas.keyvalue;



public interface DataObjectFactory {
	public DataObject createDataObject(String className);
	public DataObjectQuery createDataObjectQuery(String className);
}