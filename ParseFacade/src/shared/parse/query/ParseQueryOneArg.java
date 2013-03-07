package shared.parse.query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import shared.parse.ParseBase;

import com.parse.ParseQuery;

public abstract class ParseQueryOneArg implements InvocationHandler {

	protected ParseQuery pq;

	public ParseQueryOneArg(ParseQuery pq) {
		this.pq = pq;
	}

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
			doNameValue(name, arg);
			return null;
		}
	}

	protected abstract void doNameValue(String name, Object arg);

}