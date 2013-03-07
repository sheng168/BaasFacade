package shared.baas.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class HandlerOneArg implements InvocationHandler {
	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		String name = m.getName();
		if (args.length == 1) {
			Object arg = args[0];

			doNameValue(name, arg);
			return null;
		} else {
			throw new UnsupportedOperationException("Can only set values, not read: void something(Type arg)");
		}
	}

	protected abstract void doNameValue(String name, Object arg);

}