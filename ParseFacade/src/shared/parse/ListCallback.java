package shared.parse;

import java.util.List;

import com.parse.ParseException;

public interface ListCallback<T> {
	public void done(List<T> list);
	public void error(ParseException e);
}
