package montyPan.gxt.generator.client;

import java.util.ArrayList;

import montyPan.gxt.generator.shared.Method;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("XGenService")
public interface XGenService extends RemoteService {
	ArrayList<Method> getMethods(String className) throws Exception;
}