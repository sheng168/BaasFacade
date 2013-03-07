package shared.baas;


public interface DataStoreFacade<T> {

	/**
	 * 
	 * @return a new instance of clazz
	 */
	public T create();

	public Query<T> newQuery();

	public Query<T> newOrQuery(Query<T>... queries);

	public T wrap(ObjectData object);

}