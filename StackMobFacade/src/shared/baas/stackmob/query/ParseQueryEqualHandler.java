package shared.baas.stackmob.query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import shared.baas.stackmob.ParseBase;

import com.parse.ParseQuery;

public class ParseQueryEqualHandler implements InvocationHandler {
	ParseQuery pq;
	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		String name = m.getName();
		if (args.length == 0) {
			throw new UnsupportedOperationException("Can only set values, not read from query");
		} else {
			Object arg = args[0];
			if (arg instanceof ParseBase) {
				arg = ((ParseBase)arg).parseObject();
			}
			pq.whereEqualTo(name, arg);
			return null;
		}
	}

	public ParseQueryEqualHandler(ParseQuery pq) {
		super();
		this.pq = pq;
	}
	
}