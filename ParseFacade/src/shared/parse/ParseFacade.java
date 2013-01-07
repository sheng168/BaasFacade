package shared.parse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
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
public class ParseFacade<T> {
	Class<T> clazz;
	Class<?>[] interfaces;

	private ParseFacade(Class<T> clazz) {
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
	public static <T> ParseFacade<T> get(Class<T> clazz) {
		return new ParseFacade<T>(clazz);
	}
	
	/**
	 * 
	 * @return a new instance of clazz
	 */
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
	public Query<T> query() {
		return newQuery();
	}
	
	public Query<T> newQuery() {
//		new ParseQuery(RegionUser.class.getSimpleName())
//		.whereEqualTo("name", name)
//		.whereEqualTo("user", ParseUser.getCurrentUser())
//		.

		return new Query<T>(this);
	}
	
	public Query<T> newOrQuery(Query<T>... queries) {
//		new ParseQuery(RegionUser.class.getSimpleName())
//		.whereEqualTo("name", name)
//		.whereEqualTo("user", ParseUser.getCurrentUser())
//		.

		ArrayList<ParseQuery> list = new ArrayList<ParseQuery>();
		for (Query<T> q : queries) {
			list.add(q.pq);
		}
		
		Query<T> query = new Query<T>(this);
		query.pq = ParseQuery.or(list);
		return query;
	}
	
	public static class Query<T> {
		ParseFacade<T> facade;
		ParseQuery pq;
		
		protected Query(ParseFacade<T> parseFacade) {
			this.facade = parseFacade;
			String simpleName = parseFacade.clazz.getSimpleName();
			if ("User".equals(simpleName) || "Installation".equals(simpleName)) {
				simpleName = "_" + simpleName;
			}
			pq = new ParseQuery(simpleName);
		}

		@SuppressWarnings("unchecked")
		public T equalTo() {
			return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(), facade.interfaces, new ParseQueryEqualHandler(pq));
		}

		@SuppressWarnings("unchecked")
		public T orderAsc() {
			return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(), facade.interfaces, new ParseQueryOrderAscHandler(pq));
		}

		@SuppressWarnings("unchecked")
		public T orderDesc() {
			return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(), facade.interfaces, new ParseQueryOrderDescHandler(pq));
		}
		
		public List<T> find() throws ParseException {
			return facade.wrap(pq.find());
		}

		public void findInBackground(final ListCallback<T> callback) {
			pq.findInBackground(new FindCallback() {
				@Override
				public void done(List<ParseObject> list, ParseException e) {
					if (e == null) {
						callback.done(facade.wrap(list));
					} else {
						callback.error(e);
					}
				}
			});
		}

		public ParseQuery parseQuery() {
			return pq;
		}
	}
	
	static class ParseObjectHandler implements InvocationHandler {
		ParseObject obj;

		public ParseObjectHandler(ParseObject obj) {
			this.obj = obj;
		}

		public Object invoke(Object proxy, Method m, Object[] args)
				throws Throwable {
//			try {
				String name = m.getName();
				String key = name; //.substring(3); // assume get set
				if (args.length == 0) {
					if (ParseBase.PARSE_OBJECT.equals(name))
						return obj;
					String typeName = m.getReturnType().getName();
					if (typeName.equalsIgnoreCase("float")) {
						return (float)obj.getDouble(key);
					} else {
						Object object = obj.get(key);
						if (m.getReturnType().isPrimitive() && object == null) {
							return 0;
						} else {
							return object;
						}
					}
				} else {
					Object object = args[0];
					if (key.equals("objectId"))
						System.out.println("value " + object);
					obj.put(key, object);
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
	
	private static class ParseQueryEqualHandler implements InvocationHandler {
		ParseQuery pq;
		
		@Override
		public Object invoke(Object proxy, Method m, Object[] args)
				throws Throwable {
			String name = m.getName();
			if (args.length == 0) {
				throw new UnsupportedOperationException("Can only set values, not read from query");
			} else {
				pq.whereEqualTo(name, args[0]);
				return null;
			}
		}

		public ParseQueryEqualHandler(ParseQuery pq) {
			super();
			this.pq = pq;
		}
		
	}

	private static abstract class ParseQueryOrderHandler implements InvocationHandler {
		ParseQuery pq;
		
		@Override
		public Object invoke(Object proxy, Method m, Object[] args)
				throws Throwable {
			String name = m.getName();
			if (args.length == 0) {
				orderBy(name);
				return null;
			} else {
				throw new UnsupportedOperationException("Can only read values");
			}
		}

		abstract void orderBy(String name);

		public ParseQueryOrderHandler(ParseQuery pq) {
			this.pq = pq;
		}
		
	}
	
	private static class ParseQueryOrderDescHandler extends ParseQueryOrderHandler {
		public ParseQueryOrderDescHandler(ParseQuery pq) {
			super(pq);
		}

		@Override
		void orderBy(String name) {
			pq.orderByDescending(name);
		}

	}

	private static class ParseQueryOrderAscHandler extends ParseQueryOrderHandler {
		public ParseQueryOrderAscHandler(ParseQuery pq) {
			super(pq);
		}

		@Override
		void orderBy(String name) {
			pq.orderByAscending(name);
		}

	}
}
