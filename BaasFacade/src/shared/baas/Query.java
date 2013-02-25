package shared.baas;

import java.util.List;

public interface Query<T> {
//	@SuppressWarnings("unchecked")
	public abstract T equalTo();

//	@SuppressWarnings("unchecked")
	public abstract T orderAsc();

//	@SuppressWarnings("unchecked")
	public abstract T orderDesc();

//	@SuppressWarnings("unchecked")
	// cast to T
	public abstract T include();

	public abstract List<T> find() throws Exception;

	public abstract void findInBackground(final ListCallback<T> callback);

	public abstract void getInBackground(String objectId, GetCallback<T> callback);

}
