package montyPan.gxt.generator.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

public class XGenHome extends Composite {
	private static XGenHomeUiBinder uiBinder = GWT.create(XGenHomeUiBinder.class);
	interface XGenHomeUiBinder extends UiBinder<Widget, XGenHome> {}

	public XGenHome() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}