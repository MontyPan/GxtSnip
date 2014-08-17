package montyPan.gxt.generator.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface Template extends XTemplates {
	@XTemplate(source="event.code")
	SafeHtml eventCode(String name);
	
	@XTemplate(source="handler.code")
	SafeHtml handlerCode(String name);
}