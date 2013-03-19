package shared.baas.test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.baas.DataClassFacade;
import shared.baas.DataObject;
import shared.baas.DataQuery;


/**
 * Type safe facade for the parse.com API.  
 * Class and field names are defined using an interface instead of String(s).
 * You should only create one instance for each interface class.
 * 
 * @author sheng
 *
 * @param <T>
 */
public class StackMobFacade<T> implements DataClassFacade<T> {
	Class<T> clazz;
	Class<?>[] interfaces;

	static class SMObject extends HashMap<String, Object> {
		public String className;
		public String id;
		protected Date updatedAt;
		protected Date createdAt;

		Map<String, Object> getData() {
			return this;
		}
	}
	
	private StackMobFacade(Class<T> clazz) {
		super();
		assert clazz != null;
		assert clazz.isInterface();
		
		this.clazz = clazz;
		Class<?>[] interfaces = {clazz};
		this.interfaces = interfaces;
	}

	/**
	 * Use this to get instances for related to each class.
	 * 
	 * @param clazz interface that extends ParseBase
	 * @return may be a cache or new instance
	 */
	public static <T> StackMobFacade<T> get(Class<T> clazz) {
		return new StackMobFacade<T>(clazz);
	}
	
	/* (non-Javadoc)
	 * @see shared.parse.DataStoreFacade#create()
	 */
	@Override
	public T create() {
		SMObject po = new SMObject();
		po.className = clazz.getSimpleName();
		return wrap(po);
	}

	@SuppressWarnings("unchecked")
	public T wrap(SMObject po) {
		assert po != null;
		
		Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, 
				new StackMobHandler(po));
		return (T)obj;
	}

//	public T wrap(String objectId) {
//		ParseObject po = ParseObject.createWithoutData(clazz.getSimpleName(), objectId);
//		return wrap(po);
//	}

	public List<T> wrap(List<SMObject> l) {
		ArrayList<T> r = new ArrayList<T>();
		for (SMObject parseObject : l) {
			r.add(wrap(parseObject));
		}
		return r;
	}
	
	/* (non-Javadoc)
	 * @see shared.parse.DataStoreFacade#newQuery()
	 */
//	@Override
//	public shared.baas.Query<T> newQuery() {
//		new ParseQuery(RegionUser.class.getSimpleName())
//		.whereEqualTo("name", name)
//		.whereEqualTo("user", ParseUser.getCurrentUser())
//		.

//		return null; //new Query<T>(this);
//	}
	
	/* (non-Javadoc)
	 * @see shared.parse.DataStoreFacade#newOrQuery(shared.parse.ParseFacade.Query)
	 */
//	@Override
//	public Query<T> newOrQuery(Query<T>... queries) {
//		new ParseQuery(RegionUser.class.getSimpleName())
//		.whereEqualTo("name", name)
//		.whereEqualTo("user", ParseUser.getCurrentUser())
//		.

//		ArrayList<ParseQuery> list = new ArrayList<ParseQuery>();
//		for (Query<T> q : queries) {
//			list.add(q.pq);
//		}
		
//		Query<T> query = null; //wrap(ParseQuery.or(list));
//		return query;
//	}

	@Override
	public DataQuery<T> newQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataQuery<T> newOrQuery(DataQuery<T>... queries) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T wrap(DataObject object) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public BasicQuery<T> wrap(ParseQuery pq) {
//		Query<T> query = new Query<T>(this);
//		query.pq = pq;
//		return query;
//	}

}
