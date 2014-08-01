package montyPan.gwt.uiXml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * <code>ui.xml</code> 檔的資料結構，部份用 JDOM2 的 class、部份用自訂的 class。
 * <p>
 * XXX 並非依照 ui.xml spec 撰寫，會用什麼又有用到的才寫進去... [逃]
 */
public class UiXml {
	public static final String UIBINDER_URI = "urn:ui:com.google.gwt.uibinder";
	public static final String URN_IMPORT = "urn:import:";
	public static final String UI_WITH = "with";
	public static final String UI_IMPORT = "import";

	private Namespace uiBinderNS;
	private HashMap<Namespace, Boolean> nsMap = new HashMap<>();
	private HashMap<UiWith, Boolean> uiWithMap = new HashMap<>();
	private HashMap<UiImport, Boolean> uiImportMap = new HashMap<>();
	private ArrayList<Element> wtfList = new ArrayList<Element>();

	public UiXml(List<Namespace> nsList) {
		for (Namespace ns : nsList) {
			if (UIBINDER_URI.equals(ns.getURI())) { 
				uiBinderNS = ns;
				nsMap.put(ns, true);	//uibinder 一定用到
			} else {
				nsMap.put(ns, false);
			}
		}
	}
	
	public void addUiWith(UiWith uiWith) {
		uiWithMap.put(uiWith, false);
	}
	
	public void addUiImport(UiImport uiImport) {
		uiImportMap.put(uiImport, false);
	}
	
	public void addWtfNode(Element node) {
		wtfList.add(node);
	}

	/**
	 * 有用到這個 namespace
	 */
	public void useNS(Namespace namespace) {
		nsMap.put(namespace, true);
	}

	public void useUiWith(UiWith uiWith) {
		uiWithMap.put(uiWith, true);
	}

	public Namespace getUiBinderNS() {
		return uiBinderNS;
	}

	public Set<UiWith> getUiWith() {
		return uiWithMap.keySet();
	}
	
	public ArrayList<Namespace> getUnusedNS() {
		ArrayList<Namespace> result = new ArrayList<Namespace>();
		for (Namespace ns : nsMap.keySet()) {
			if (!nsMap.get(ns)) {
				result.add(ns);
			}
		}
		return result;
	}
	
	public ArrayList<UiWith> getUnusedUiWith() {
		ArrayList<UiWith> result = new ArrayList<UiWith>();
		for (UiWith uiWith : uiWithMap.keySet()) {
			if (!uiWithMap.get(uiWith)) {
				result.add(uiWith);
			}
		}
		return result;
	}

	public ArrayList<Element> getWtfList() {
		return wtfList;
	}

	public UiWith findUiWithByField(String value) {
		for (UiWith uiWith : getUiWith()) {
			if (value.startsWith(uiWith.getField())) {	//不用全字相等，因為有 {message.title} 這種情況
				return uiWith;
			}
		}
		return null;	//XXX 目前預計處理的範圍中是不會出現 null 的 \囧/
	}
}