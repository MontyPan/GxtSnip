package montyPan.gwt.toolApp;

import java.io.File;

import org.jdom2.Namespace;

import montyPan.gwt.uiXml.UiWith;
import montyPan.gwt.uiXml.UiXmlParser;

public class UiXmlChecker {
	public static void main(String[] args) throws Exception {
		File source = new File("FOO.ui.xml");
		UiXmlParser parser = new UiXmlParser(source);
		MarkdownOutput result = new MarkdownOutput();
		
		if (parser.getUnusedNS().size() != 0) {
			result.addH3("沒有用到的 Namespace");
			for (Namespace ns : parser.getUnusedNS()) {
				result.addBullet(ns.getPrefix() + "=\"" + ns.getURI() + "\"");
			}
			result.nextBlock();
		}
		
		if (parser.getUnusedUiWith().size() != 0) {
			result.addH3("沒有用到的 ui:with");
			for (UiWith uiWith : parser.getUnusedUiWith()) {
				result.addBullet(uiWith.getField() + " (" + uiWith.getType() + ")");
			}
			result.nextBlock();
		}
		
		System.out.print(result);
	}
}

class MarkdownOutput {
	private StringBuffer result = new StringBuffer();
	
	public void nextBlock() {
		result.append("\n\n");
	}
	
	public void addLine(String line) {
		result.append(line + "\n");
	}
	
	public void addH3(String title) {
		addLine("### " + title + " ###");
	}
	
	public void addBullet(String context) {
		addBullet(0, context);
	}

	public void addBullet(int level, String context) {
		StringBuffer tmp = new StringBuffer();
		for (int i = 0; i < level; i++) {
			tmp.append("\t");
		}
		addLine(tmp + "* " + context);		
	}

	@Override
	public String toString() {
		return result.toString();
	}
}