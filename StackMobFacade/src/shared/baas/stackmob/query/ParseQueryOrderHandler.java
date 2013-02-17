package shared.baas.stackmob.query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.parse.ParseQuery;

abstract class ParseQueryOrderHandler implements InvocationHandler {
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