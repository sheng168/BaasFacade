package shared.baas.stackmob.query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import shared.baas.ObjectBase;

import com.parse.ParseQuery;

public class ParseQueryIncludeHandler implements InvocationHandler {
	private String prefix = "";
	ParseQuery pq;
	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		String name = m.getName();
		
		if ("toString".equals(name)) {
			return this.toString();
		}
		
		if (args.length == 0) {
			include(name);
			
			final Class<?> returnType = m.getReturnType();
			if (ObjectBase.class.isAssignableFrom(returnType)) {
				// support chaining
				String prefix = name + ".";				
				final Class[] interfaces = {returnType};
				System.out.println("ParseQueryIncludeHandler.invoke()" + interfaces);
				final Object pInst = Proxy.newProxyInstance(returnType.getClassLoader(), interfaces, new ParseQueryIncludeHandler(prefix, pq));
				
				System.out.println("chain " + prefix);
				return pInst;
			} else {
				return null;
			}
		} else {
			throw new UnsupportedOperationException("Can only read values");
		}
	}

	void include(String name) {
		final String string = prefix + name;
		System.out.println("include:" + string);
		pq.include(string);
	}

	public ParseQueryIncludeHandler(ParseQuery pq) {
		this.pq = pq;
	}

	public ParseQueryIncludeHandler(String prefix, ParseQuery pq2) {
		this(pq2);
		this.prefix = prefix;
	}
	
}