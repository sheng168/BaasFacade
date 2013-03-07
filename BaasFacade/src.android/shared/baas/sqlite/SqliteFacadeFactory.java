package shared.baas.sqlite;

import shared.baas.DataStoreFacade;
import shared.baas.FacadeFactory;
import android.content.Context;
import android.net.Uri;

public class SqliteFacadeFactory extends FacadeFactory {
//	static SqliteFacadeFactory instance = new SqliteFacadeFactory();
	
	Context ctx;
	Uri baseUri;

	public SqliteFacadeFactory(Context ctx, Uri baseUri) {
		super();
		this.ctx = ctx;
		this.baseUri = baseUri;
	}

	/* (non-Javadoc)
	 * @see shared.parse.FacadeFactory#get(java.lang.Class)
	 */
	@Override
	public <T> DataStoreFacade<T> get(Class<T> clazz) {
		return new SqliteFacade<T>(this, clazz);
	}
	
}
