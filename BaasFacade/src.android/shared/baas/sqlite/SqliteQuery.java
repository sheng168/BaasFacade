package shared.baas.sqlite;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import shared.baas.ListCallback;
import shared.baas.Query;
import shared.baas.impl.HandlerOneArg;
import shared.baas.impl.HandlerZeroArg;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class SqliteQuery<T> implements Query<T> {
	SqliteFacade<T> facade;
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	StringBuilder orderBy = new StringBuilder();
	
//	ParseQuery pq;

	protected SqliteQuery(SqliteFacade<T> parseFacade) {
		this.facade = parseFacade;
//		String simpleName = parseFacade.getClazz().getSimpleName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T equalTo() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(),
				facade.getInterfaces(), new HandlerOneArg() {					
					@Override
					protected void doNameValue(String name, Object arg) {
						qb.appendWhere(name + " = ?");
					}
				});
	}

	@SuppressWarnings("unchecked")
	public T notEqualTo() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(),
				facade.getInterfaces(), new HandlerOneArg() {					
					@Override
					protected void doNameValue(String name, Object arg) {
						qb.appendWhere(name + " <> ?");
					}
				});
	}

	@Override
	@SuppressWarnings("unchecked")
	public T orderAsc() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(), 
				facade.getInterfaces(), new HandlerZeroArg() {
					@Override
					protected void doNameValue(String name) {
						if (orderBy.length() > 0) {
							orderBy.append(", ");
						}
						
						orderBy.append(name).append(" asc");
					}
				});
	}

	@Override
	@SuppressWarnings("unchecked")
	public T orderDesc() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(), 
				facade.getInterfaces(), new HandlerZeroArg() {
					@Override
					protected void doNameValue(String name) {
						if (orderBy.length() > 0) {
							orderBy.append(", ");
						}
						
						orderBy.append(name).append(" desc");
					}
				});
	}

	@Override
	@SuppressWarnings("unchecked")
	// cast to T
	public T include() {
		return (T) Proxy.newProxyInstance(facade.getClazz().getClassLoader(), 
				facade.getInterfaces(), new HandlerZeroArg() {
					@Override
					protected void doNameValue(String name) {
						System.out.println("include " + name);
					}
				});
	}

//	@Override
//	public List<T> find() throws ParseException {
//		return facade.wrap(pq.find());
//	}

//	public void findInBackground(final ListCallback<T> callback) {
//		pq.findInBackground(new FindCallback() {
//			@Override
//			public void done(List<ParseObject> list, ParseException e) {
//				if (e == null) {
//					callback.done(facade.wrap(list));
//				} else {
//					callback.error(e);
//				}
//			}
//		});
//	}

	public void getInBackground(String objectId,
			final shared.parse.GetCallback<T> callback) {
//		pq.getInBackground(objectId, new GetCallback() {
//			@Override
//			public void done(ParseObject o, ParseException e) {
//				if (e == null)
//					callback.done(facade.wrap(o));
//				else
//					callback.error(e);
//			}
//		});
	}


	@Override
	public void findInBackground(final ListCallback<T> callback) {
		try {
			callback.done(find());
		} catch (Exception e) {
			callback.equals(e);
		}
		
	}

	@Override
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
	public List<T> find() throws Exception {
		Uri uri = Uri.withAppendedPath(facade.sqliteFacadeFactory.baseUri, facade.getClazz().getSimpleName());
		
		String sortOrder = orderBy.length() == 0 ? null : orderBy.toString();
		String selection = null;
		String[] selectionArgs = null;
		final Cursor c = facade.sqliteFacadeFactory.ctx.getContentResolver().query(uri, null, selection, selectionArgs, sortOrder);
		
		final ArrayList<T> list = new ArrayList<T>();
		
		for (int i = 0; i < c.getCount(); i++) {
			list.add(facade.wrap(new SqliteObjectDataWithCursor<T>(facade.sqliteFacadeFactory, facade.getClazz().getSimpleName(), c, i)));
		}
		return list;
	}

	@Override
	public String toString() {
		return "SqliteQuery [facade=" + facade + ", qb=" + qb + ", orderBy="
				+ orderBy + "]";
	}
}

