package montyPan.gwt.uiXml;

import java.io.File;
import java.util.ArrayList;

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
		
		boolean moreFlag = false;
		
		//只處理 ui node，所以看到 ui:with、ui:import 要跳過
		for (Element child : root.getChildren()) {
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
	 */
	private static String toFieldValue(String attr) {
		//學理上要從左右大括號位置作 substring()，但是這樣的 code 也不錯 [茶]
		String result = attr.trim();
		return result.substring(1, result.length()-1);
	}
}