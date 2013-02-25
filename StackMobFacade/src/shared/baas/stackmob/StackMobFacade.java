package shared.baas.stackmob;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import shared.baas.DataStoreFacade;
import shared.baas.ObjectBase;

import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Type safe facade for the parse.com API.  
 * Class and field names are defined using an interface instead of String(s).
 * You should only create one instance for each interface class.
 * 
 * @author sheng
 *
 * @param <T>
 */
public class StackMobFacade<T> implements DataStoreFacade<T> {
	Class<T> clazz;
	Class<?>[] interfaces;

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
		ParseObject po = new ParseObject(clazz.getSimpleName());
		return wrap(po);
	}

	@SuppressWarnings("unchecked")
	public T wrap(ParseObject po) {
		assert po != null;
		assert clazz.getSimpleName().equals(po.getClassName());
		
		Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, 
				new ParseObjectHandler(po));
		return (T)obj;
	}

	public T wrap(String objectId) {
		ParseObject po = ParseObject.createWithoutData(clazz.getSimpleName(), objectId);
		return wrap(po);
	}

	public List<T> wrap(List<ParseObject> l) {
		ArrayList<T> r = new ArrayList<T>();
		for (ParseObject parseObject : l) {
			r.add(wrap(parseObject));
		}
		return r;
	}
	
	/**
	 * @deprecated rename to newQuery to be clearer
	 * @return
	 */
	@Deprecated
	public BasicQuery<T> query() {
		return newQuery();
	}
	
	/* (non-Javadoc)
	 * @see shared.parse.DataStoreFacade#newQuery()
	 */
	@Override
	public BasicQuery<T> newQuery() {
//		new ParseQuery(RegionUser.class.getSimpleName())
//		.whereEqualTo("name", name)
//		.whereEqualTo("user", ParseUser.getCurrentUser())
//		.

		return new Query<T>(this);
	}
	
	/* (non-Javadoc)
	 * @see shared.parse.DataStoreFacade#newOrQuery(shared.parse.ParseFacade.Query)
	 */
	@Override
	public BasicQuery<T> newOrQuery(Query<T>... queries) {
//		new ParseQuery(RegionUser.class.getSimpleName())
//		.whereEqualTo("name", name)
//		.whereEqualTo("user", ParseUser.getCurrentUser())
//		.

		ArrayList<ParseQuery> list = new ArrayList<ParseQuery>();
		for (Query<T> q : queries) {
			list.add(q.pq);
		}
		
		BasicQuery<T> query = wrap(ParseQuery.or(list));
		return query;
	}
	
	public BasicQuery<T> wrap(ParseQuery pq) {
		Query<T> query = new Query<T>(this);
		query.pq = pq;
		return query;
	}
	
	static class ParseObjectHandler implements InvocationHandler {
		ParseObject obj;

		public ParseObjectHandler(ParseObject obj) {
			this.obj = obj;
		}

		@Override
		public Object invoke(Object proxy, Method m, Object[] args)
				throws Throwable {
//			try {
				String name = m.getName();
				String key = name; //.substring(3); // assume get set
				if (args.length == 0) {
					if (ObjectBase.PARSE_OBJECT.equals(name))
						return obj;
				final Class<?> returnType = m.getReturnType();
				String typeName = returnType.getName();
					if (typeName.equalsIgnoreCase("float")) {
						return (float)obj.getDouble(key);
					} else {
						Object object = obj.get(key);
					if (returnType.isPrimitive() && object == null) {
							return 0;
					} else if (object != null
							&& ParseObject.class.isInstance(object)
							&& ObjectBase.class.isAssignableFrom(returnType)) {
						return ((StackMobFacade<?>)StackMobFacade.get(returnType)).wrap(
								(ParseObject) object);
					} else if (object != null && !returnType.isInstance(object)
							&& !returnType.isPrimitive()) {
							return null;
						} else {
							return object;
						}
					}
				} else {
					// set value
					Object object = args[0];
					if (key.equals("objectId"))
						System.out.println("value " + object);
					
					if (object instanceof ObjectBase) {
						obj.put(key, ((ObjectBase) object).parseObject());
					} else {
						obj.put(key, object);
					}
					
					return null;
				}
//			} catch (InvocationTargetException e) {
//				throw e.getTargetException();
//			} catch (Exception e) {
//				throw e;
//			}
			// return something
		}

		@Override
		public String toString() {
			return super.toString() + " " + obj;
		}
	}
}
