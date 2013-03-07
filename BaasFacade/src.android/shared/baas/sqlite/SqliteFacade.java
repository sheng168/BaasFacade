package shared.baas.sqlite;


import shared.baas.DataStoreFacade;
import shared.baas.Query;
import shared.baas.impl.AbstractFacade;

/**
 * Type safe facade for the parse.com API. Class and field names are defined
 * using an interface instead of String(s). You should only create one instance
 * for each interface class.
 * 
 * @author sheng
 * 
 * @param <T>
 */
public class SqliteFacade<T> extends AbstractFacade<T> implements DataStoreFacade<T> {

	SqliteFacadeFactory sqliteFacadeFactory;

	public SqliteFacade(SqliteFacadeFactory sqliteFacadeFactory, Class<T> clazz) {
		super(clazz);
		this.sqliteFacadeFactory = sqliteFacadeFactory;
	}

	protected SqliteObjectData<T> newObjectData() {
		return new SqliteObjectData<T>(sqliteFacadeFactory, clazz.getSimpleName());
	}

	@Override
	public Query<T> newQuery() {
		return new SqliteQuery<T>(this);
	}

	@Override
	public Query<T> newOrQuery(Query<T>... queries) {
		// TODO Auto-generated method stub
		return null;
	}


}
