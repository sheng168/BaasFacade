package shared.baas;

import shared.baas.stackmob.ParseFacade;
import shared.baas.stackmob.ParseFacade.Query;

public interface DataStoreFacade<T> {

	/**
	 * 
	 * @return a new instance of clazz
	 */
	public abstract T create();

	public abstract Query<T> newQuery();

	public abstract Query<T> newOrQuery(Query<T>... queries);

}