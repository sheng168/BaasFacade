package shared.baas;

import shared.baas.stackmob.BasicQuery;
import shared.baas.stackmob.Query;
import shared.baas.stackmob.StackMobFacade;

public interface DataStoreFacade<T> {

	/**
	 * 
	 * @return a new instance of clazz
	 */
	public abstract T create();

	public abstract BasicQuery<T> newQuery();

	public abstract BasicQuery<T> newOrQuery(Query<T>... queries);

}