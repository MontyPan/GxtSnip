package montyPan.gwt.uiXml;

import org.jdom2.Element;

public class UiImport {
	public static final String FIELD_NAME = "field";
	
	public static UiImport decode(Element element) {
		UiImport result = new UiImport();
		result.field = element.getAttributeValue(FIELD_NAME);
		return result;
	}
	
	private String field;

	public String getField() { return field; }
}