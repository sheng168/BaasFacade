package shared.baas.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class HandlerZeroArg implements InvocationHandler {	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		String name = m.getName();
		if (args.length == 0) {
			return doNameValue(name);
		} else {
			throw new UnsupportedOperationException("can only handle: void something()");
		}
	}

	protected abstract Object doNameValue(String name);

}