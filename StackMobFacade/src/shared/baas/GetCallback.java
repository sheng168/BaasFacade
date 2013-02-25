package shared.baas;


public interface GetCallback<T> {
	public void done(T o);
	public void error(Exception e);
}
