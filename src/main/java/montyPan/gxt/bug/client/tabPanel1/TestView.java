package montyPan.gxt.bug.client.tabPanel1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.Radio;

public class TestView extends Composite {
	private static final Margins margins = new Margins(5, 0, 0, 5);
	private static final VerticalLayoutData layoutData = new VerticalLayoutData(1, -1, margins);

	private static TestViewUiBinder uiBinder = GWT.create(TestViewUiBinder.class);
	interface TestViewUiBinder extends UiBinder<Widget, TestView> {}

	@UiField VerticalLayoutContainer selectPanel;
	@UiField ContentPanel setupPanel;
		
	public TestView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		for (int i = 0; i < 5; i++) {
			Radio item = new Radio();
			item.setBoxLabel("" + i);
			item.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (event.getValue()) {
						setupPanel.clear();
						setupPanel.add(new TestOI());
						setupPanel.forceLayout();
					}
				}
			});
			selectPanel.add(item, layoutData);
		}
	}
}