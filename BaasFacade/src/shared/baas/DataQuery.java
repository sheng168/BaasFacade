package shared.baas;

import java.util.List;

public interface DataQuery<T> {
	public abstract T equalTo();

	public abstract T orderAsc();
	public abstract T orderDesc();

	public abstract T include();

	public abstract List<T> find() throws Exception;

	public abstract void findInBackground(final ListCallback<T> callback);

//	public abstract void getInBackground(String objectId, GetCallback<T> callback);

}
