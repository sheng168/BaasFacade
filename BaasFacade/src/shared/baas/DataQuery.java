package shared.baas;

import java.util.List;

import shared.baas.keyvalue.ListenableFuture;

public interface DataQuery<T> {
	public abstract T equalTo();

	public abstract T orderAsc();
	public abstract T orderDesc();

	public abstract T include();

//	ListenableFuture<List<T>> find() throws Exception;

//	public abstract List<T> find() throws Exception;

//	public abstract void findInBackground(final ListCallback<T> callback);

//	public abstract void getInBackground(String objectId, GetCallback<T> callback);

}
