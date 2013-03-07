package shared.baas.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import shared.baas.FacadeFactory;
import shared.baas.ObjectBase;
import shared.baas.ObjectData;
//import shared.parse.ParseBase;
//import shared.parse.ParseFacade;
//import com.parse.ParseObject;

public class InterfaceHandler implements InvocationHandler {
	ObjectData obj;

	public InterfaceHandler(ObjectData obj) {
		this.obj = obj;
	}

	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		// try {
		String key = m.getName();
		
		if (args.length == 0) {
			// built-in
			if (ObjectBase.OBJECT.equals(key))
				return obj;
			if ("toString".equals(key))
				return obj.toString();
//			if (ParseBase.PARSE_OBJECT.equals(key))
//				return obj;
			
			// user define
			final Class<?> returnType = m.getReturnType();
			String typeName = returnType.getName();
			if (typeName.equalsIgnoreCase("float")) {
				return obj.get(key, returnType);
			} else {
				Object object = obj.get(key, returnType);
				if (returnType.isPrimitive() && object == null) {
					return 0;
				} else if (object != null
						&& ObjectData.class.isInstance(object)
						&& ObjectBase.class.isAssignableFrom(returnType)) {
					FacadeFactory factory = obj.getFactory();					
					return (factory.get(returnType))
							.wrap((ObjectData) object);
				} else if (object != null && !returnType.isInstance(object)
						&& !returnType.isPrimitive()) {
					return null;
				} else {
					return object;
				}
			}
		} else if (args.length == 1) { // 
			// set value
			Object object = args[0];
//			if (key.equals("objectId"))
//				System.out.println("value " + object);

			if (object instanceof ObjectBase) {
				obj.put(key, ((ObjectBase) object).objectData());
			} else {
				obj.put(key, object);
			}

			return null;
		} else {
			throw new IllegalArgumentException("only 0 or 1 param is valid: " + args.length);
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
