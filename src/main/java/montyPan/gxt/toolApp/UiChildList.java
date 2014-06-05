package montyPan.gxt.toolApp;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

import com.google.gwt.uibinder.client.UiChild;
import com.sencha.gxt.widget.core.client.Component;

public class UiChildList {
	public static void main(String[] args) {
		StringBuffer result = new StringBuffer();
		result.append("> GXT 版本：3.1.0\n\n\n");
		
		Reflections reflections = new Reflections("com.sencha.gxt.widget.core.client");
		Set<Class<? extends Component>> subTypes = reflections.getSubTypesOf(Component.class);
		HashMap<Package, ArrayList<Class<? extends Component>>> pkgMap = new HashMap<>();
		
		for (Class<? extends Component> clazz : subTypes) {
			ArrayList<Class<? extends Component>> list = pkgMap.get(clazz.getPackage());
			
			if (list == null) { 
				list = new ArrayList<>();
				pkgMap.put(clazz.getPackage(), list);
			}
			
			list.add(clazz);
		}
		
		for (Package pkg : pkgMap.keySet()) {
			result.append("## " + pkg.getName() + " ##\n");
			
			ArrayList<Class<? extends Component>> list = pkgMap.get(pkg);
			Collections.sort(list, new Comparator<Class<? extends Component>>() {
				@Override
				public int compare(Class<? extends Component> o1, Class<? extends Component> o2) {
					return o1.getSimpleName().compareTo(o2.getSimpleName());
				}
			});
			
			for (Class<? extends Component> clazz : pkgMap.get(pkg)) {
				//XXX 不知道為什麼會有空的 name，所以乾脆就先跳過 XD
				if (clazz.getSimpleName().length() == 0) { continue; }
				
				result.append("### " + clazz.getSimpleName() + " ###\n");
				result.append(insert(clazz.getMethods()));
			}
		}
		
		try {
			Files.write(
				new File("docs", "UiChildList.md").toPath(), 
				result.toString().getBytes("UTF-8"), 
				StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE_NEW
			);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String insert(Method[] methods) {
		//不能用 method.getName() 來排序，因為真正看的是 tagName
		//所以只好先存另一個 HashMap，然後全部弄完再一起轉字串 Orz
		HashMap<String, String> map = new HashMap<>();
		
		for (Method method : methods) {
			UiChild uiChild = (UiChild) method.getAnnotation(UiChild.class);
			if (uiChild == null) { continue; }
			
			String tagName = uiChild.tagname();
			if (tagName.length() == 0) {
				//因為 UiChild 預設是套用在 addFoo() 上，所以沒有 tagName 就直接取 Foo
				tagName = method.getName().substring(3).toLowerCase();
			}
			
			StringBuffer methodResult = new StringBuffer();
			methodResult.append("* " + tagName);
			methodResult.append("\n");
			
			methodResult.append("\t* 實際 method：" + method.getName());
			methodResult.append("\n");
			
			methodResult.append("\t* 可出現次數：" + (uiChild.limit() == -1 ? "不限制" : "" + uiChild.limit()));
			methodResult.append("\n\n\n");
			map.put(tagName, methodResult.toString());
		}
		
		ArrayList<String> keyList = new ArrayList<>();
		keyList.addAll(map.keySet());
		Collections.sort(keyList);
		
		StringBuffer result = new StringBuffer();		
		for (String key : keyList) {
			result.append(map.get(key));
		}
		
		return result.toString();
	}
}