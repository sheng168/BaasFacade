package shared.baas.sqlite;

import shared.baas.DataClassFacade;
import shared.baas.DataFacadeFactory;
import android.content.Context;
import android.net.Uri;

public class SqliteFacadeFactory extends DataFacadeFactory {
	// package implementation details
	final Context ctx;
	final Uri baseUri;

	/**
	 * 
	 * @param ctx which contains getContentResolver();
	 * @param baseUri which we'll append classNames to
	 */
	public SqliteFacadeFactory(Context ctx, Uri baseUri) {
		super();
		this.ctx = ctx; 
		this.baseUri = baseUri;
		
//		ctx.getContentResolver().insert(Uri.withAppendedPath(baseUri, pathSegment), values);
	}

	/* (non-Javadoc)
	 * @see shared.parse.FacadeFactory#get(java.lang.Class)
	 */
	@Override
	public <T> DataClassFacade<T> get(Class<T> clazz) {
		return new SqliteFacade<T>(this, clazz);
	}
	
}
