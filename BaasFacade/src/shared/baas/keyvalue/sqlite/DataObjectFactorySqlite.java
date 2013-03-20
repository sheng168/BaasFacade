package shared.baas.keyvalue.sqlite;

import android.content.ContentResolver;
import android.net.Uri;
import shared.baas.DataClassFacade;
import shared.baas.DataFacadeFactory;
import shared.baas.impl.BasicDataClassFacade;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectFactory;
import shared.baas.keyvalue.DataObjectQuery;

public class DataObjectFactorySqlite extends DataObjectFactory {
	ContentResolver cr;
	Uri baseUri;

	public DataObjectFactorySqlite(ContentResolver cr, Uri baseUri) {
		super();
		this.cr = cr;
		this.baseUri = baseUri;
	}

	@Override
	public DataObject createDataObject(String className) {
		return new DataObjectSqlite(cr, baseUri, className);
	}

	@Override
	public DataObjectQuery createDataObjectQuery(String className) {
		return new DataObjectQuerySqlite(cr, baseUri, className);
	}
	
	public <T> DataClassFacade<T> get(Class<T> clazz) {
		return new BasicDataClassFacade<T>(this, clazz);
	}
}