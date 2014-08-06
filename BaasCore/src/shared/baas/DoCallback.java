package shared.baas;


public abstract class DoCallback {
	public void done(Exception e) {
		if (e == null)
			done();
		else
			error(e);
	}
	abstract public void done();
	abstract public void error(Exception e);
}
