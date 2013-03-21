package shared.baas;

import java.util.List;

import shared.baas.keyvalue.ListenableFuture;

public abstract class DataQuery<T> {
	public abstract T equalTo();

	public abstract T orderAsc();
	public abstract T orderDesc();

	public abstract T include();

	public ListenableFuture<List<T>> find() {
		throw new UnsupportedOperationException();
	}

//	public abstract List<T> find() throws Exception;

//	public abstract void findInBackground(final ListCallback<T> callback);

//	public abstract void getInBackground(String objectId, GetCallback<T> callback);

}
