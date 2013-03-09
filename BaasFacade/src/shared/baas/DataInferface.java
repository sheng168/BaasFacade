package shared.baas;



/**
 * Extend this interface to define you ParseObject class field.
 * Don't use primitive types if you may be reading fields with null values.
 * Supported types include boolean, int, long, double (not float), String, Date.
 * If you never write to a field(even a primitive one) it'll have a null value rather than a default value.
 * Don't try to read the built-in fields(objectId, createdAt, updatedAt) through this interface.
 * @author sheng
 *
 */
public interface DataInferface {
	static final String OBJECT = "dataObject";
//	static final String SAVE = "saveAsync";
//	static final String DELETE = "deleteAsync";
	
	/**
	 * @deprecated in case of emergency only
	 * @return the backing object
	 */
//	@Deprecated
//	Object unwrap();
	
	@Deprecated
	@Override
	String toString();
	
	DataObject dataObject();
//	String objectId();
//	void objectId(String in);
//	
//	Date createdAt();
//	Date updatedAt();
//	
//	Future<? extends ObjectBase> saveAsync(GetCallback<? extends ObjectBase> getCallback);
//	Future<? extends ObjectBase> deleteAsync(GetCallback<? extends ObjectBase> getCallback);

}
