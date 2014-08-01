package montyPan.gwt.uiXml;

import java.util.List;

import org.jdom2.Element;

public class UiWith {
	public static final String TYPE_NAME = "type";
	public static final String FIELD_NAME = "field";
	
	public static UiWith decode(Element element) {
		UiWith result = new UiWith();
		result.field = element.getAttributeValue(FIELD_NAME);
		result.type = element.getAttributeValue(TYPE_NAME);
		result.children = element.getChildren();
		return result;
	}
	
	private String field;
	private String type;
	private List<Element> children;

	public String getField() { return field; }
	
	public String getType() { return type; }
	
	public List<Element> getChildren() { return children; }
}