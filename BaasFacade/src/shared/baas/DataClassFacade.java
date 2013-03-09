package shared.baas;


public interface DataClassFacade<T> {

	/**
	 * 
	 * @return a new instance of clazz
	 */
	public T create();

	public DataQuery<T> newQuery();

	public DataQuery<T> newOrQuery(DataQuery<T>... queries);

	public T wrap(DataObject object);

	
}