package shared.baas.stackmob;

import java.util.List;

import shared.baas.ListCallback;

import com.parse.ParseException;
import com.parse.ParseQuery;

public interface BasicQuery<T> {

	@SuppressWarnings("unchecked")
	public abstract T equalTo();

	@SuppressWarnings("unchecked")
	public abstract T orderAsc();

	@SuppressWarnings("unchecked")
	public abstract T orderDesc();

	@SuppressWarnings("unchecked")
	// cast to T
	public abstract T include();

	public abstract List<T> find() throws ParseException;

	public abstract void findInBackground(final ListCallback<T> callback);

	public abstract void getInBackground(String objectId,
			final shared.baas.GetCallback<T> callback);

	public abstract ParseQuery parseQuery();

}