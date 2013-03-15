package shared.baas.keyvalue;

import java.util.Set;

import shared.baas.DoCallback;

public interface DataObject {
	public Set<String> keySet();
	
	public <T> T get(String key, Class<T> type);
	public void put(String key, Object value);

//	public abstract ParseACL getACL();

//	public abstract String getClassName();

//	public abstract Date getCreatedAt();
//	public abstract Date getUpdatedAt();
//
//	public abstract String getObjectId();
//	public abstract void setObjectId(String newObjectId);

//	public abstract boolean isDataAvailable();

	public abstract void refreshInBackground(DoCallback callback);

	public ListenableFuture<String> save();

	public abstract void deleteInBackground(DoCallback callback);

//	public abstract void setACL(ParseACL acl);

//	public abstract DataFacadeFactory getFactory();
}