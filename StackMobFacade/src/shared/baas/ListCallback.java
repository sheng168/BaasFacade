package shared.baas;

import java.util.List;

public interface ListCallback<T> {
	public void done(List<T> list);
	public void error(Exception e);
}
