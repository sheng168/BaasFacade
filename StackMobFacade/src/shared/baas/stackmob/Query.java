package shared.baas.stackmob;

import java.lang.reflect.Proxy;
import java.util.List;

import shared.baas.ListCallback;
import shared.baas.stackmob.query.ParseQueryEqualHandler;
import shared.baas.stackmob.query.ParseQueryIncludeHandler;
import shared.baas.stackmob.query.ParseQueryOrderAscHandler;
import shared.baas.stackmob.query.ParseQueryOrderDescHandler;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Query<T> implements BasicQuery<T> {
	StackMobFacade<T> facade;
	ParseQuery pq;
	
	protected Query(StackMobFacade<T> parseFacade) {
		this.facade = parseFacade;
		String simpleName = parseFacade.clazz.getSimpleName();
		if ("User".equals(simpleName) || "Installation".equals(simpleName)) {
			simpleName = "_" + simpleName;
		}
		pq = new ParseQuery(simpleName);
	}

	/* (non-Javadoc)
	 * @see shared.baas.stackmob.BasicQuery#equalTo()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T equalTo() {
		return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(), facade.interfaces, new ParseQueryEqualHandler(pq));
	}

	/* (non-Javadoc)
	 * @see shared.baas.stackmob.BasicQuery#orderAsc()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T orderAsc() {
		return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(), facade.interfaces, new ParseQueryOrderAscHandler(pq));
	}

	/* (non-Javadoc)
	 * @see shared.baas.stackmob.BasicQuery#orderDesc()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T orderDesc() {
		return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(), facade.interfaces, new ParseQueryOrderDescHandler(pq));
	}
	
	/* (non-Javadoc)
	 * @see shared.baas.stackmob.BasicQuery#include()
	 */
	@Override
	@SuppressWarnings("unchecked") // cast to T
	public T include() {
		return (T) Proxy.newProxyInstance(facade.clazz.getClassLoader(), facade.interfaces, new ParseQueryIncludeHandler(pq));
	}

	/* (non-Javadoc)
	 * @see shared.baas.stackmob.BasicQuery#find()
	 */
	@Override
	public List<T> find() throws ParseException {
		return facade.wrap(pq.find());
	}

	/* (non-Javadoc)
	 * @see shared.baas.stackmob.BasicQuery#findInBackground(shared.baas.ListCallback)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see shared.baas.stackmob.BasicQuery#getInBackground(java.lang.String, shared.baas.GetCallback)
	 */
	@Override
	public void getInBackground(String objectId, final shared.baas.GetCallback<T> callback) {
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

	/* (non-Javadoc)
	 * @see shared.baas.stackmob.BasicQuery#parseQuery()
	 */
	@Override
	public ParseQuery parseQuery() {
		return pq;
	}
}