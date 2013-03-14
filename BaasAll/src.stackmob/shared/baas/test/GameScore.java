package shared.baas.test;

import shared.baas.DataInferface;


/**
 * Extend this interface to define you ParseObject class field.
 * Don't use primitive types if you may be reading fields with null values.
 * Supported types include boolean, int, long, double (not float), String, Date.
 * If you never write to a field(even a primitive one) it'll have a null value rather than a default value.
 * Don't try to read the built-in fields(objectId, createdAt, updatedAt) through this interface.
 * @author sheng
 *
 */
public interface GameScore extends DataInferface {
	String name();
	void name(String in);

	int score();
	void score(int i);

}
