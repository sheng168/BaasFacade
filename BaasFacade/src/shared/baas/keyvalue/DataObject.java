package shared.baas.keyvalue;

import java.util.Date;
import java.util.Set;

public abstract class DataObject {
	public static final String OBJECT_ID = "objectId"; // String
	public static final String CREATED_AT = "createddate"; // Long
	public static final String UPDATED_AT = "lastmoddate"; // Long

	public abstract Set<String> keySet();
	
	public abstract <T> T get(String key, Class<T> type);
	public abstract void put(String key, Object value);

//	public abstract String getClassName();

	public Date getCreatedAt() {
		return get(CREATED_AT, Date.class);
	}
	public Date getUpdatedAt() {
		return get(UPDATED_AT, Date.class);
	}
//
	public abstract String getObjectId();
//	public abstract void setObjectId(String newObjectId);

//	public abstract boolean isDataAvailable();

//	public abstract void refreshInBackground(DoCallback callback);

	/**
	 * Save this object, probably in a background thread.  
	 * You can handle save result using callback by setting a listener.
	 * Or you can call get() to wait until save is done.
	 * 
	 * @return ListenableFuture with objectId
	 */
	public abstract ListenableFuture<String> save();
	
	/**
	 * it's better to use a field to mark record as deleted in a lot of cases.
	 * @return
	 */
	public ListenableFuture<Void> delete() {
		throw new UnsupportedOperationException();
	}
}