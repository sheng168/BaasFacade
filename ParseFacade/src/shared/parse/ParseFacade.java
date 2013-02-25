package shared.parse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import shared.parse.query.ParseQueryEqualHandler;
import shared.parse.query.ParseQueryIncludeHandler;
import shared.parse.query.ParseQueryOrderAscHandler;
import shared.parse.query.ParseQueryOrderDescHandler;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Type safe facade for the parse.com API. Class and field names are defined
 * using an interface instead of String(s). You should only create one instance
 * for each interface class.
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
		Class<?>[] interfaces = { clazz };
		this.interfaces = interfaces;
	}

	/**
	 * Use this to get instances for related to each class.
	 * 
	 * @param clazz
	 *            interface that extends ParseBase
	 * @return may be a cache or new instance
	 */
	public static <T> ParseFacade<T> get(Class<T> clazz) {
		return new ParseFacade<T>(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shared.parse.DataStoreFacade#create()
	 */
	// @Override
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
		return (T) obj;
	}

	public T wrap(String objectId) {
		ParseObject po = ParseObject.createWithoutData(clazz.getSimpleName(),
				objectId);
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
	public Query<T> query() {
		return newQuery();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shared.parse.DataStoreFacade#newQuery()
	 */
	// @Override
	public Query<T> newQuery() {
		// new ParseQuery(RegionUser.class.getSimpleName())
		// .whereEqualTo("name", name)
		// .whereEqualTo("user", ParseUser.getCurrentUser())
		// .

		return new Query<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * shared.parse.DataStoreFacade#newOrQuery(shared.parse.ParseFacade.Query)
	 */
	// @Override
	public Query<T> newOrQuery(Query<T>... queries) {
		// new ParseQuery(RegionUser.class.getSimpleName())
		// .whereEqualTo("name", name)
		// .whereEqualTo("user", ParseUser.getCurrentUser())
		// .

		ArrayList<ParseQuery> list = new ArrayList<ParseQuery>();
		for (Query<T> q : queries) {
			list.add(q.pq);
		}

		Query<T> query = wrap(ParseQuery.or(list));
		return query;
	}

	public Query<T> wrap(ParseQuery pq) {
		Query<T> query = new Query<T>(this);
		query.pq = pq;
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
			return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(),
					facade.interfaces, new ParseQueryEqualHandler(pq));
		}

		@SuppressWarnings("unchecked")
		public T orderAsc() {
			return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(),
					facade.interfaces, new ParseQueryOrderAscHandler(pq));
		}

		@SuppressWarnings("unchecked")
		public T orderDesc() {
			return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(),
					facade.interfaces, new ParseQueryOrderDescHandler(pq));
		}

		@SuppressWarnings("unchecked")
		// cast to T
		public T include() {
			return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(),
					facade.interfaces, new ParseQueryIncludeHandler(pq));
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

		public void getInBackground(String objectId,
				final shared.parse.GetCallback<T> callback) {
			pq.getInBackground(objectId, new GetCallback() {
				@Override
				public void done(ParseObject o, ParseException e) {
					if (e == null)
						callback.done(facade.wrap(o));
					else
						callback.error(e);
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

		@Override
		public Object invoke(Object proxy, Method m, Object[] args)
				throws Throwable {
			// try {
			String name = m.getName();
			String key = name; // .substring(3); // assume get set
			if (args.length == 0) {
				if ("objectData".equals(name))
					return new ParseObjectData(obj);
				if (ParseBase.PARSE_OBJECT.equals(name))
					return obj;
				final Class<?> returnType = m.getReturnType();
				String typeName = returnType.getName();
				if (typeName.equalsIgnoreCase("float")) {
					return (float) obj.getDouble(key);
				} else {
					Object object = obj.get(key);
					if (returnType.isPrimitive() && object == null) {
						return 0;
					} else if (object != null
							&& ParseObject.class.isInstance(object)
							&& ParseBase.class.isAssignableFrom(returnType)) {
						return ((ParseFacade<?>) ParseFacade.get(returnType))
								.wrap((ParseObject) object);
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

				if (object instanceof ParseBase) {
					obj.put(key, ((ParseBase) object).parseObject());
				} else {
					obj.put(key, object);
				}

				return null;
			}
			// } catch (InvocationTargetException e) {
			// throw e.getTargetException();
			// } catch (Exception e) {
			// throw e;
			// }
			// return something
		}

		@Override
		public String toString() {
			return super.toString() + " " + obj;
		}
	}
}
