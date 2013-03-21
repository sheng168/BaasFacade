package shared.baas.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import shared.baas.DataInterface;

class StackMobHandler implements InvocationHandler {
	StackMobFacade.SMObject obj;

	public StackMobHandler(StackMobFacade.SMObject obj) {
		this.obj = obj;
	}

	@Override
	public Object invoke(final Object proxy, Method m, Object[] args) // j2se behave different than android
			throws Throwable {
		// try {
		String name = m.getName();
		String key = name; // .substring(3); // assume get set
		if (args == null || args.length == 0) {
			if (DataInterface.OBJECT.equals(name))
				return obj;
			if ("toString".equals(name)) {
				return obj.toString();
			}
			if ("objectId".equals(name)) {
				return obj.id;
			}

			final Class<?> returnType = m.getReturnType();
			String typeName = returnType.getName();
			if (typeName.equalsIgnoreCase("float")) {
				return (float) getDouble(obj, key);
			} else {
				Object object = get(obj, key);
				if (returnType.isPrimitive() && object == null) {
					return 0;
				} else if (object != null
						&& StackMobFacade.SMObject.class.isInstance(object)
						&& DataInterface.class.isAssignableFrom(returnType)) {
					return ((StackMobFacade<?>) StackMobFacade.get(returnType))
							.wrap((StackMobFacade.SMObject) object);
				} else if (object != null && !returnType.isInstance(object)
						&& !returnType.isPrimitive()) {
					return null;
				} else {
					return object;
				}
			}
		} else { // one arg
			/*if (DataInferface.SAVE.equals(name)) {
				if (args[0] instanceof GetCallback) {
					final GetCallback call = (GetCallback) args[0];
					StackMob.getStackMob().getDatastore().post(obj.className, obj, new StackMobCallback() {
						@Override
						public void failure(StackMobException e) {
							e.printStackTrace();
							call.error(e);
						}

						@Override
						public void success(String s) {
							System.out.println(s);
							JsonObject json = new JsonParser().parse(s).getAsJsonObject();
							
							long mod = json.get("lastmoddate").getAsLong();
							long create = json.get("createddate").getAsLong();
							String cn = obj.className;
							String id = json.get(cn.toLowerCase()+"_id").getAsString();
							obj.id = id;
							obj.updatedAt = new Date(mod);
							obj.createdAt = new Date(create);
							
							call.done(proxy);
						}
					});
					return null;
				}
			}
			if (ObjectBase.DELETE.equals(name)) {
				if (args[0] instanceof GetCallback) {
					final GetCallback call = (GetCallback) args[0];
					StackMob.getStackMob().getDatastore().delete(obj.className, obj.id, new StackMobCallback() {
						@Override
						public void failure(StackMobException e) {
							e.printStackTrace();
							call.error(e);
						}

						@Override
						public void success(String s) {
							System.out.println("delete:"+s);
							call.done(proxy);
						}
					});
					return null;
				}
			}*/

			// set value
			Object object = args[0];
			if (key.equals("objectId")) {
				System.out.println("value " + object);
				obj.id = (String) object;
			}

			if (object instanceof DataInterface) {
				put(obj, key, (object));
			} else {
				put(obj, key, object);
			}

			return null;
		}
		// } catch (InvocationTargetException e) {
		// throw e.getTargetException();
		// } catch (Exception e) {
		// throw e;
		// }
		// return something
	}

	private void put(Object obj2, String key, Object object) {
		obj.put(key, object);
	}

	private void put(Object obj2, String key, DataInterface objectBase) {
		System.out.println(key + "=" + objectBase);
	}

	private Object get(Object obj2, String key) {
		return obj.get(key);
	}

	private double getDouble(Object obj2, String key) {
		final Object o = obj.get(key);
		if (o instanceof Number)
			return ((Number) o).doubleValue();
		else
			return 0;
	}

	@Override
	public String toString() {
		return super.toString() + " " + obj;
	}
}