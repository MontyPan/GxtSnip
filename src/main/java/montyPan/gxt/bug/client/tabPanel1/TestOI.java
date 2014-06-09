package montyPan.gxt.bug.client.tabPanel1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class TestOI implements IsWidget {
	private static TestOIUiBinder uiBinder = GWT.create(TestOIUiBinder.class);
	interface TestOIUiBinder extends UiBinder<Widget, TestOI> {}

	@Override
	public Widget asWidget() {
		return uiBinder.createAndBindUi(this);
	}
}