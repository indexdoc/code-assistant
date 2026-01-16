package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 
 * @author hongliang.dinghl Dom4j 生成XML文档与解析XML文档
 */
public class Dom4jDemo {

	public void createXml(String fileName) {
		
		File file=new File(fileName);
		System.out.println(file.getAbsolutePath());
		
//		Document document = DocumentHelper.createDocument();
//		Element employees = document.addElement("employees");
//		Element employee = employees.addElement("employee");
//		Element name = employee.addElement("name");
//		name.setText("ddvip");
//		Element sex = employee.addElement("sex");
//		sex.setText("m");
//		Element age = employee.addElement("age");
//		age.setText("29");
		Document doc = DocumentHelper.createDocument();
		
		Element root = doc.addElement("TplSet");
		
		root.addAttribute("name", "name1");
		root.addAttribute("outpath", "outPath");
		
		Element tpls = root.addElement("tpls");
		
		Timestamp ts = new Timestamp(0);

		try {
			Writer fileWriter = new FileWriter(fileName);
			XMLWriter xmlWriter = new XMLWriter(fileWriter);
			xmlWriter.write(doc);
			xmlWriter.close();
		} catch (IOException e) {

			System.out.println(e.getMessage());
		}

	}

	public void parserXml(String fileName) {
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element employees = document.getRootElement();
			for (Iterator i = employees.elementIterator(); i.hasNext();) {
				Element employee = (Element) i.next();
				for (Iterator j = employee.elementIterator(); j.hasNext();) {
					Element node = (Element) j.next();
					System.out.println(node.getName() + ":" + node.getText());
				}

			}
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("dom4j parserXml");
	}
	public static void main(String[] args) {
		Dom4jDemo d = new Dom4jDemo();
		d.createXml(".\\test.xml");
		d.parserXml(".\\test.xml");		
	}
}