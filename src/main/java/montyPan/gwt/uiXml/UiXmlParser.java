package montyPan.gwt.uiXml;

import java.io.File;
import java.util.ArrayList;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

public class UiXmlParser {
	private UiXml uiXml;
	
	public UiXmlParser(File source) throws Exception {
		Element root = new SAXBuilder().build(source).getRootElement();
		uiXml = new UiXml(root.getNamespacesIntroduced());
		
		for (Element uiWith : root.getChildren(UiXml.UI_WITH, uiXml.getUiBinderNS())) {
			uiXml.addUiWith(UiWith.decode(uiWith));			
		}
		
		//ui:with 都建完之後，檢查 uiWith 彼此互相 reference 的狀況
		analyseUiWith();
		
		for (Element uiImport : root.getChildren(UiXml.UI_IMPORT, uiXml.getUiBinderNS())) {
			uiXml.addUiImport(UiImport.decode(uiImport));
		}
		
		boolean moreFlag = false;
		for (Element child : root.getChildren()) {
			//只處理 ui node，所以看到 ui:with、ui:import 要跳過
			if (child.getNamespace() == uiXml.getUiBinderNS()) {
				continue;
			}
			
			if (moreFlag) {
				uiXml.addWtfNode(child);
				continue;
			}
			
			moreFlag = true;
			parseUI(child);
		}
	}

	private void analyseUiWith() {
		for (UiWith uiWith : uiXml.getUiWith()) {
			for (Element child : uiWith.getChildren()) {
				for (Attribute attr : child.getAttributes()) {
					String value = toFieldValue(attr.getValue());
					if (value == null) { continue; }
					uiXml.useUiWith(uiXml.findUiWithByField(value));
				}
			}
		}
	}
	
	private void parseUI(Element node) {
		uiXml.useNS(node.getNamespace());
				
		//有設定 layoutData
		if (node.getAttributeValue("layoutData") != null) {
			String layoutData = toFieldValue(node.getAttributeValue("layoutData"));
			
			for (UiWith uiWith : uiXml.getUiWith()) {
				if (layoutData.equals(uiWith.getField())) {
					uiXml.useUiWith(uiWith);
					//TODO 檢查 layoutData 合理性
				}
			}
		}
		
		for (Element child : node.getChildren()) {
			parseUI(child);
		}
	}
	
	public ArrayList<Namespace> getUnusedNS() {
		return uiXml.getUnusedNS();
	}

	public ArrayList<UiWith> getUnusedUiWith() {
		return uiXml.getUnusedUiWith();
	}

	public ArrayList<Element> getWtfList() {
		return uiXml.getWtfList();
	}

	/**
	 * 從 attribute 的設定值還原回變數值，也就是
	 * 從 <code>{foo}</code> 還原回 <code>foo</code>。
	 * <p>
	 * 實驗結果：attribute 前後可空白、但是大括號內不可有空白。
	 * 
	 * @param attr
	 * @return 如果可以是變數，回傳還原後的變數值；反之回傳 null
	 */
	private static String toFieldValue(String attr) {
		//學理上要從左右大括號位置作 substring()，但是這樣的 code 也不錯 [茶]
		String result = attr.trim();
		if (result.startsWith("{")) {
			return result.substring(1, result.length()-1);
		} else {
			return null;
		}
	}
}