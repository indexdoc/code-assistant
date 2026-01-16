package util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class Config {

	private Properties prop = null;
	private String fileName = null;

	public Config(String filename) {
		readPropertiesFile(filename);
		this.fileName = filename;
	}

	public void readPropertiesFile(String filename) {
		try {
			// 读取属性文件a.properties
			prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(filename));
			prop.load(in); /// 加载属性列表
			// Iterator<String> it =
			// ConfigUtil.prop.stringPropertyNames().iterator();
			// while (it.hasNext()) {
			// String key = it.next();
			// System.out.println(key + ":" + ConfigUtil.prop.getProperty(key));
			// }
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String getProperty(String key) {
		String value = prop.getProperty(key);
		if (value == null)
			value = "";
		return value;
	}

	public void setProperty(String key, String value) {
		prop.setProperty(key, value);
	}
	
	public void save(){
		this.savePropertiesFile(this.fileName);
	}

	public void savePropertiesFile(String filename) {
		try {
			FileOutputStream oFile = new FileOutputStream(filename);
			prop.store(oFile, DateUtil.getCurrentDateStr());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

