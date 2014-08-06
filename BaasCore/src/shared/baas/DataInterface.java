package shared.baas;

import java.util.Date;

import shared.baas.keyvalue.DataObject;



/**
 * Extend this interface to define your class fields.
 * Don't use primitive types if you may be reading fields with null values.
 * Supported types include boolean, int, long, double (not float), String, Date.
 * If you never write to a field(even a primitive one) it'll have a null value rather than a default value.
 * Don't try to read the built-in fields(objectId, createdAt, updatedAt) through this interface.
 * @author sheng
 *
 */
public interface DataInterface {
	static final String OBJECT = "dataObject";
	
	@Override
	String toString();
	
	DataObject dataObject();
	String objectId();
	void objectId(String in);
	
	Date createdAt(); // needed for sorting
	Date updatedAt();
	
	void createdAt(Date in); // needed for filtering
	void updatedAt(Date in);
}
