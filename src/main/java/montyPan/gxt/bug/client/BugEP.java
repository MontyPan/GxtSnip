package montyPan.gxt.bug.client;

import montyPan.gxt.bug.client.tabPanel1.TestRoot;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class BugEP implements EntryPoint {
	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new TestRoot());
	}
}