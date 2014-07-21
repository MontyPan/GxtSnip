package montyPan.gxt.bug.client.reuseMargin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

public class ReuseMargin extends Composite {
	private static ReuseMarginUiBinder uiBinder = GWT.create(ReuseMarginUiBinder.class);
	interface ReuseMarginUiBinder extends UiBinder<Widget, ReuseMargin> {}

	public ReuseMargin() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}