package entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


public class TplSet {
	//文件后缀名为.tplset
	static public String SUFFIX=".tplset";
	
	//只保存全路径名，不再保存名字
//	private String name;
	
	//全路径名字
	private String fullPathFileName;
	
	private String outPath;
	
//	private HashMap<String,Tpl> mTpl = new HashMap<String,Tpl>();
	
	private ArrayList<Tpl> alTpl = new ArrayList<Tpl>();
	
	public String getFullPathFileName(){
		return fullPathFileName;
	}
	
	public void addTpl(Tpl e){
		int i=0;
		for (Tpl tpl:alTpl){
			if (tpl.getName().equals(e.getName())){
				alTpl.remove(i);
				alTpl.add(i, e);
				this.save();				
				return;
			}
			++i;
		}
		alTpl.add(e);
		this.save();
	}
	
	public Tpl getTpl(String name){
		for (Tpl tpl:alTpl){
			if (tpl.getName().equals(name))
				return tpl;
		}
		return null;
	}
	public Collection<Tpl> tpls(){
		return alTpl;
	}
	public void saveToFile(String filename) throws IOException{
		File file=new File(filename);
		this.fullPathFileName = file.getAbsolutePath();
		
		Document doc = DocumentHelper.createDocument();
		
		Element root = doc.addElement("TplSet");
		
		root.addAttribute("OutPath", outPath);
		
		for (Tpl v:alTpl){
			Element e = root.addElement("tpl");
			e.addAttribute("Name",v.getName());
			e.addAttribute("Type",v.getType());
			e.addAttribute("NameRegular",v.getNameRegular());
			e.addAttribute("Path",v.getPath());
			e.addAttribute("Checked", v.isChecked()?"true":"false");
			e.addAttribute("Remark", v.getRemark());
		}
		Writer fileWriter = new FileWriter(fullPathFileName);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8"); 
		XMLWriter xmlWriter = new XMLWriter(fileWriter,format);
		xmlWriter.write(doc);
		xmlWriter.close();
	}
	public void readFile(String filename) throws DocumentException{
		File file=new File(filename);
		this.fullPathFileName = file.getAbsolutePath();

		SAXReader reader=new SAXReader();
		Document doc=reader.read(file);
		Element root=doc.getRootElement();
		
		this.outPath = root.attributeValue("OutPath");
		
		alTpl = new ArrayList<Tpl>();
		
		@SuppressWarnings("unchecked")
		Iterator<Element> it=root.elementIterator();
		int i = 0;
		while (it.hasNext()){
			Element e = it.next();
			Tpl t = new Tpl();
			t.setName(e.attributeValue("Name"));
			t.setType(e.attributeValue("Type"));
			t.setNameRegular(e.attributeValue("NameRegular"));
//			t.setPath(e.attributeValue("Path"));
			t.setPath(this.path());
			t.setChecked(e.attributeValue("Checked").compareTo("true")==0);
			t.setRemark(e.attributeValue("Remark"));
			t.setOrder(i++);
			alTpl.add(t);
		}
	}

	public String singleName() {
		File f = new File(this.fullPathFileName);
		return f.getName();
	}
	public String path(){
		File f= new File(this.fullPathFileName);
		return f.getParent();
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public void delTpl(String name) {
//		mTpl.remove(name);
		for (Tpl tpl:alTpl){
			if (tpl.getName().equals(name)){
				alTpl.remove(tpl);
				break;
			}
		}
		this.save();
	}

	public ArrayList<Tpl> getSelectedTpls() {
		ArrayList<Tpl> al = new ArrayList<Tpl>();
		for (Tpl t:alTpl){
			if (t.isChecked())
				al.add(t);
		}
		return al;
	}

	public void save() {
		try {
			saveToFile(this.getFullPathFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
