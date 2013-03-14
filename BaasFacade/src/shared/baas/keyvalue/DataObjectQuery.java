package shared.baas.keyvalue;

import shared.baas.ListCallback;


public interface DataObjectQuery {

	void whereEqualTo(String key, Object value);

	void whereNotEqualTo(String key, Object value);

	void findInBackground(ListCallback<DataObject> callback);

	void whereGreaterThan(String key, Object value);

	void orderByAsc(String key);

	void orderByDesc(String key);

	void isInRange(int skip, int count);

}