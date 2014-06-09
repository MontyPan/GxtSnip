package montyPan.gxt.bug.client.tabPanel1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class TestRoot implements IsWidget {
	private static TestRootUiBinder uiBinder = GWT.create(TestRootUiBinder.class);
	interface TestRootUiBinder extends UiBinder<Widget, TestRoot> {}
	
	@UiField TabPanel root;
	
	@Override
	public Widget asWidget() {
		Viewport result = new Viewport();
		result.add(uiBinder.createAndBindUi(this));
		return result;
	}
}