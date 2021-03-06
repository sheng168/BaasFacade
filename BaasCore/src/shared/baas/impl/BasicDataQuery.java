package shared.baas.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import shared.baas.DataInterface;
import shared.baas.DataQuery;
import shared.baas.keyvalue.DataObject;
import shared.baas.keyvalue.DataObjectQuery;
import shared.baas.keyvalue.ListenableFuture;
import shared.baas.keyvalue.ListenableFuture.Basic;
import shared.baas.keyvalue.ListenableFuture.Listener;

public class BasicDataQuery<T> extends DataQuery<T> {
	BasicDataClassFacade<T> facade;
//	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//	StringBuilder orderBy = new StringBuilder();
	DataObjectQuery keyValueQuery;
	
//	ParseQuery pq;

	protected BasicDataQuery(BasicDataClassFacade<T> facade, DataObjectQuery keyValueQuery) {
		this.facade = facade;
		this.keyValueQuery = keyValueQuery;
//		String simpleName = parseFacade.getClazz().getSimpleName();
	}

//	@Override
	@Override
	public ListenableFuture<List<T>> find() {
		final Basic<List<T>> future = new ListenableFuture.Basic<List<T>>();
		
		ListenableFuture<List<DataObject>> find = keyValueQuery.find();
		find.setListener(new Listener<List<DataObject>>() {				
			@Override
			public void done(List<DataObject> value, Exception e) {
				if (e == null) {
					final ArrayList<T> list = new ArrayList<T>();
					
					for (DataObject dataObject : value) {
						list.add(facade.wrap(dataObject));
					}
					
					future.set(list, null);
				} else {
					future.set(null, e);
				}
			}
		});
		
		return future;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T equalTo() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(),
				facade.getInterfaces(), new HandlerOneArg() {					
					@Override
					protected void doNameValue(String name, Object arg) {
//						qb.appendWhere(name + " = ?");
						if (arg instanceof DataInterface) {
							arg = ((DataInterface)arg).dataObject();
						}
						keyValueQuery.whereEqualTo(name, arg);
					}
				});
	}

	@Override
	@SuppressWarnings("unchecked")
	public T notEqualTo() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(),
				facade.getInterfaces(), new HandlerOneArg() {					
					@Override
					protected void doNameValue(String name, Object arg) {
//						qb.appendWhere(name + " <> ?");
						keyValueQuery.whereNotEqualTo(name, arg);
					}
				});
	}

	@Override
	@SuppressWarnings("unchecked")
	public T orderAsc() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(), 
				facade.getInterfaces(), new HandlerZeroArg() {
					@Override
					protected Object doNameValue(String name) {
						keyValueQuery.orderByAsc(name);
						return null;
					}
				});
	}

	@Override
	@SuppressWarnings("unchecked")
	public T orderDesc() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(), 
				facade.getInterfaces(), new HandlerZeroArg() {
					@Override
					protected Object doNameValue(String name) {
						keyValueQuery.orderByDesc(name);
						return null;
					}
				});
	}

	private final class HandlerForInclude extends HandlerZeroArg {
		String prefix;
		
		public HandlerForInclude(String prefix) {
			this.prefix = prefix;
		}
		
		@Override
		protected Object doNameValue(String name) {			
			return null;
		}

		@Override
		public Object invoke(Object proxy, Method m, Object[] args)
				throws Throwable {
			super.invoke(proxy, m, args); // check
			
			String name = prefix + m.getName();
			keyValueQuery.include(name);
			
			String prefix = name + ".";				
			final Class<?>[] interfaces = {m.getReturnType()};
			final Object pInst = Proxy.newProxyInstance(m.getReturnType().getClassLoader(), interfaces, new HandlerForInclude(prefix));
			return pInst;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	// cast to T
	public T include() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(), 
				facade.getInterfaces(), new HandlerForInclude(""));
	}

//	@Override
	public void getInBackground(String objectId,
			final shared.baas.GetCallback<T> callback) {
//		pq.getInBackground(objectId, new GetCallback() {
//			@Override
//			public void done(ParseObject o, ParseException e) {
//				if (e == null)
//					callback.done(facade.wrap(o));
//				else
//					callback.error(e);
//			}
//		});
//
	}

	@Override
	public DataObjectQuery dataObjectQuery() {
		return keyValueQuery;
	}
}

