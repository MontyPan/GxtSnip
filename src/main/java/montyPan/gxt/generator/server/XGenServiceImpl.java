package montyPan.gxt.generator.server;

import java.util.ArrayList;

import montyPan.gxt.generator.client.XGenService;
import montyPan.gxt.generator.shared.Method;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class XGenServiceImpl extends RemoteServiceServlet implements XGenService {
	@Override
	public ArrayList<Method> getMethods(String className) throws Exception {
		ArrayList<Method> result = new ArrayList<>();
		
		Class<?> clazz = Class.forName(className);
		
		for (java.lang.reflect.Method method : clazz.getMethods()) {
			Method m = new Method();
			m.setName(method.getName());
			m.setType(method.getReturnType().getSimpleName());
			result.add(m);
		}
		return result;
	}
}