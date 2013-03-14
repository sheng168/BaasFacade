package shared.baas.keyvalue;

import shared.baas.DoCallback;

public interface DataObject {

//	public abstract ParseACL getACL();

//	public abstract String getClassName();

//	public abstract Date getCreatedAt();
//	public abstract Date getUpdatedAt();
//
//	public abstract String getObjectId();
//	public abstract void setObjectId(String newObjectId);

//	public abstract boolean isDataAvailable();

	public abstract void refreshInBackground(DoCallback callback);

	public abstract void saveInBackground(DoCallback callback);

	public abstract void deleteInBackground(DoCallback callback);

//	public abstract void setACL(ParseACL acl);

	public Object get(String key, Class<?> type);
	public void put(String key, Object value);
//	public abstract DataFacadeFactory getFactory();
}