package shared.baas.stackmob;

import com.parse.ParseException;

public interface GetCallback<T> {
	public void done(T o);
	public void error(ParseException e);
}
