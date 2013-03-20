package shared.baas.sqlite;


import shared.baas.DataClassFacade;
import shared.baas.DataQuery;
import shared.baas.impl.AbstractDataClassFacade;

/**
 * Type safe facade for the parse.com API. Class and field names are defined
 * using an interface instead of String(s). You should only create one instance
 * for each interface class.
 * 
 * @author sheng
 * 
 * @param <T>
 */
public class SqliteFacade<T> extends AbstractDataClassFacade<T> implements DataClassFacade<T> {

	SqliteFacadeFactory sqliteFacadeFactory;

	public SqliteFacade(SqliteFacadeFactory sqliteFacadeFactory, Class<T> clazz) {
		super(clazz);
		this.sqliteFacadeFactory = sqliteFacadeFactory;
	}

	protected SqliteObjectData newObjectData() {
		return new SqliteObjectData(sqliteFacadeFactory, clazz.getSimpleName());
	}

	@Override
	public DataQuery<T> newQuery() {
		return new SqliteQuery<T>(this);
	}

	@Override
	public DataQuery<T> newOrQuery(DataQuery<T>... queries) {
		// TODO Auto-generated method stub
		return null;
	}


}
