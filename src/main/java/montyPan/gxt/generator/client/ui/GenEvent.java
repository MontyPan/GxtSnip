package montyPan.gxt.generator.client.ui;

import montyPan.gxt.generator.client.XGenEP;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

public class GenEvent extends Composite {
	private static GenEventUiBinder uiBinder = GWT.create(GenEventUiBinder.class);
	interface GenEventUiBinder extends UiBinder<Widget, GenEvent> {}
	
	@UiField TextField name;
	@UiField TextArea eventResult;
	@UiField TextArea handlerResult;
	
	public GenEvent() {
		initWidget(uiBinder.createAndBindUi(this));
		name.addValidator(new EmptyValidator<String>());
	}
	
	@UiHandler("name")
	void changeName(ValueChangeEvent<String> vce) {
		if (!name.validate()) { return; }
		
		String n = vce.getValue().trim();
		eventResult.setValue(XGenEP.template.eventCode(n).asString());
		handlerResult.setValue(XGenEP.template.handlerCode(n).asString());
	}
}