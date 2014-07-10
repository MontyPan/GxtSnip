package montyPan.gxt.generator.client;

import montyPan.gxt.generator.client.ui.GenPropertyAccess;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class XGenEP implements EntryPoint {
	@Override
	public void onModuleLoad() {
		Viewport vp = new Viewport();
		vp.add(new GenPropertyAccess());
		RootPanel.get().add(vp);
	}
}