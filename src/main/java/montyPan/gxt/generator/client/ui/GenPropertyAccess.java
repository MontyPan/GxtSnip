package montyPan.gxt.generator.client.ui;

import java.util.ArrayList;

import montyPan.gxt.generator.client.XGenService;
import montyPan.gxt.generator.client.XGenServiceAsync;
import montyPan.gxt.generator.shared.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GenPropertyAccess extends Composite {
	private static GenPropertyAccessUiBinder uiBinder = GWT.create(GenPropertyAccessUiBinder.class);
	interface GenPropertyAccessUiBinder extends UiBinder<Widget, GenPropertyAccess> {}

	@UiField TextField className;
	@UiField TextArea code;
	@UiField(provided=true) ComboBox<Method> modelKey;
	@UiField(provided=true) ComboBox<Method> label;
	
	private static final Properties prop = GWT.create(Properties.class);
	private ListStore<Method> methodStore = new ListStore<>(prop.key());
	
	public GenPropertyAccess() {
		modelKey = new ComboBox<>(methodStore, prop.label());
		label = new ComboBox<>(methodStore, prop.label());
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("queryBtn")
	void selectQuery(SelectEvent se) {
		XGenServiceAsync rpc = GWT.create(XGenService.class);
		rpc.getMethods(className.getValue(), new AsyncCallback<ArrayList<Method>>() {
			@Override
			public void onSuccess(ArrayList<Method> result) {
				setMethods(result);
				genBtn.setEnabled(true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("查詢錯誤 [死]\n\n請輸入完整 class name（包含 package）");
				genBtn.setEnabled(false);
			}
		});
	}
	
	@UiField TextButton genBtn;
	@UiHandler("genBtn")
	void selectGen(SelectEvent se) {
		gen();
	}

	private void setMethods(ArrayList<Method> methods) {
		for (Method m : methods) {
			if (m.getName().startsWith("get")) {
				if (m.getName().equals("getClass")) { continue; }
				methodStore.add(m);
			}
		}
	}
	
	private void gen() {
		String name = className.getValue();
		name = name.substring(name.lastIndexOf('.') + 1);
		StringBuffer result = new StringBuffer();
		result.append("interface Properties extends PropertyAccess<" + name + "> {\n");
		
		if (modelKey.getValue() != null) {
			result.append("\t@Path(\"" + pureName(modelKey.getValue()) + "\")\n");
			result.append("\tModelKeyProvider<" + name + "> key();\n");
		}
		
		if (label.getValue() != null) {
			result.append("\t@Path(\"" + pureName(label.getValue()) + "\")\n");
			result.append("\tLabelProvider<" + name + "> label();\n");
		}
		
		for (Method m : methodStore.getAll()) {
			result.append("\tValueProvider<" + name + ", " + m.getType() + "> " + pureName(m) + "();\n");
		}
		result.append("}\n");
		code.setValue(result.toString());
	}
	
	private String pureName(Method m) {
		return m.getName().substring(3).toLowerCase();
	}

	interface Properties extends PropertyAccess<Method> {
		@Path("name")
		ModelKeyProvider<Method> key();
		@Path("name")
		LabelProvider<Method> label();
		ValueProvider<Method, String> name();
		ValueProvider<Method, String> type();
	}
}