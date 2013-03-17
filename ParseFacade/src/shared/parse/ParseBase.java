package shared.parse;

import shared.baas.DataInterface;

import com.parse.ParseObject;

/**
 * Extend this interface to define you ParseObject class field.
 * Don't use primitive types if you may be reading fields with null values.
 * Supported types include boolean, int, long, double (not float), String, Date.
 * If you never write to a field(even a primitive one) it'll have a null value rather than a default value.
 * Don't try to read the built-in fields(objectId, createdAt, updatedAt) through this interface.
 * @author sheng
 *
 */
public interface ParseBase extends DataInterface {
	static final String PARSE_OBJECT = "parseObject";
	
	/**
	 * @deprecated 
	 * @return the backing object
	 */
	@Deprecated
	ParseObject parseObject();
	
//	String objectId();
//	void objectId(String in);
//	
//	@Override
//	Date createdAt();
//	@Override
//	Date updatedAt();
	
//	ObjectData objectData();
}
