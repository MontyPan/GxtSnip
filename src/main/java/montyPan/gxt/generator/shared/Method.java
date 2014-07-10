package montyPan.gxt.generator.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Method implements IsSerializable {
	private String name;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}