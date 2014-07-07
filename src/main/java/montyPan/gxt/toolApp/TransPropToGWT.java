package montyPan.gxt.toolApp;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * GWT 可以允許 Messages.properties 檔案直接使用 UTF-8 撰寫內容，
 * 而不用依循古老 Java properties 檔的規範（每個字都得轉成 unicode 編碼）。
 * 所以就產生了這個東西，來轉換過去的時代眼淚 [淚目]
 * 
 * @see <a href="http://www.gwtproject.org/doc/latest/DevGuideI18n.html#DevGuideStaticStringInternationalization">reference</a>
 * @author monty.pan
 */
public class TransPropToGWT {
	public static void main(String[] args) {
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		File f = fc.getSelectedFile();
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream(f));
			
			//為了讓結果看起來好看一點，所以先把 key 排序
			ArrayList<String> keyList = new ArrayList<>();
			for (Object key : prop.keySet()) {
				keyList.add(key.toString());
			}	
			Collections.sort(keyList);
			
			ArrayList<String> result = new ArrayList<>();
			//為了讓 Eclipse 等 editor 可以順利 show 正確的內容，所以補上 BOM
			result.add("\ufeff");
			for (String key : keyList) {
				result.add(key + " = " + prop.getProperty(key) );
			}
			Files.write(f.toPath(), result, Charset.forName("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
}