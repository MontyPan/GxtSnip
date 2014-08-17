package montyPan.gxt.generator.client;

import montyPan.gxt.generator.client.ui.XGenHome;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class XGenEP implements EntryPoint {
	public static final Template template = GWT.create(Template.class);
	
	@Override
	public void onModuleLoad() {
		Viewport vp = new Viewport();
		vp.add(new XGenHome());
		RootPanel.get().add(vp);
	}
}