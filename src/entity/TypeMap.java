package entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import util.parse.CAFun;

public class TypeMap {

	// 文件名称
	private String fileName;

	// "数据库"+ "字段类型"+ "目标语言" 得到目标语言数据类型和备注
	private HashMap<ArrayList<String>, Integer> map = new HashMap<ArrayList<String>, Integer>();
	
	private ArrayList<String[]> content = new ArrayList<String[]>();
    private Set<String> dbSet ;
    private Set<String> langSet;

	static public String[] HEADER_NAME = { "数据库", "字段类型", "目标语言", "语言数据类型",
			"备注" };
	static public String[] HEADER_CODE = { "DataBase", "DataBase_DataType",
			"Lang", "Lang_DataType", "Remark" };
	static public String[] DEFAULT_DBMS ={"Oracle","MySQL","PostgreSQL","Sybase","SQL Server","Informix"};
	static public String[] DEFAULT_LANGS ={"C","C++","Java","C#","VB","JavaScript","Perl","Python","Ruby","PHP","HTML","Objective-C","ActionScript"};
	
	public TypeMap(){
		iniDbLangSet();
	}
	private void iniDbLangSet(){
	    dbSet = new HashSet<String>();
	    langSet = new HashSet<String>();

		for (String s:DEFAULT_DBMS){
			dbSet.add(s.toUpperCase());
		}
		for (String s:DEFAULT_LANGS){
			langSet.add(s.toUpperCase());
		}
		
	}
	private void refreshMap(){
		map = new HashMap<ArrayList<String>, Integer>();
		for(int i=0;i<content.size();++i){
			String[] v = content.get(i);
			map.put(tokey(v), new Integer(i));
		}
	}
	private ArrayList<String> tokey(String[] v){
		ArrayList<String> k = new ArrayList<String>();
		k.add(v[0].toUpperCase());k.add(v[1].toUpperCase());k.add(v[2].toUpperCase());
		return k;
	}
	private ArrayList<String> tokey(String db,String dbdt,String lang){
		ArrayList<String> k = new ArrayList<String>();
		k.add(db.toUpperCase());k.add(dbdt.toUpperCase());k.add(lang.toUpperCase());
		return k;
	}
	private Integer add(String[] v){
		Integer idx = map.get(tokey(v));
		if (!v[0].trim().equals(""))
			dbSet.add(v[0].toUpperCase());
		if (!v[2].trim().equals(""))
			langSet.add(v[2].toUpperCase());
		if (idx == null) {
			content.add(v);
			map.put(tokey(v), content.size() - 1);
		} else {
			String[] row = content.get(idx);
			row[3] = v[3];
			row[4] = v[4];
		}
		return idx;		
	}
	public Integer add(String db, String dbdt, String lang, String langdt, String remark) {
//		dbdt = UMFunc.getRawDataType(dbdt);
		String[] v = { db.toUpperCase(), dbdt.toUpperCase(), lang.toUpperCase(), langdt, remark };
		return add(v);
	}
	public void remove(String db,String dbdt,String lang){
		Integer delid = map.get(tokey(db,dbdt,lang));
		if (delid == null)
			return;
		map.remove(tokey(db,dbdt,lang));
		content.remove(delid);
		this.refreshMap();
	}
	public Integer get(String db, String dbdt, String lang){
		return map.get(tokey(db,dbdt,lang));
	}

	public String getLangDataType(String db, String dbdt, String lang) {
		Integer idx = map.get(tokey(db,dbdt,lang));
		if (idx == null)
			return null;
		return content.get(idx)[3];
	}
	public String getLangDataType(String dbdt,String lang){
		String langdt;
		for (String dbms:dbSet){
			langdt = getLangDataType(dbms,dbdt,lang);
			if (langdt != null){
				return langdt;
			}
		}
		return null;
	}

	public String getRemark(String db, String dbdt, String lang) {
		Integer idx = map.get(tokey(db,dbdt,lang));
		return content.get(idx)[4];
	}

	public ArrayList<String[]> getContent() {
		return content;
	}

	public ArrayList<String[]> toTableWithHeader() {
		ArrayList<String[]> table = new ArrayList<String[]>();
		table.add(HEADER_NAME);
		table.addAll(content);
		return table;
	}

	public void saveToFile(String filename) throws IOException {
		this.fileName = filename;
		Document doc = DocumentHelper.createDocument();

		Element root = doc.addElement("TypeMap");
//		ArrayList<Integer> al = new ArrayList<Integer>(map.values());
//		Collections.sort(al);
		for (String[] v : content) {
//			String[] v = content.get(i);
			Element e = root.addElement("Row");
			e.addAttribute(HEADER_CODE[0], v[0]);
			e.addAttribute(HEADER_CODE[1], v[1]);
			e.addAttribute(HEADER_CODE[2], v[2]);
			e.addAttribute(HEADER_CODE[3], v[3]);
			e.addAttribute(HEADER_CODE[4], v[4]);
		}
		Writer fileWriter = new FileWriter(filename);
        OutputFormat format = OutputFormat.createPrettyPrint();   
        format.setEncoding("UTF-8");   
		XMLWriter xmlWriter = new XMLWriter(fileWriter,format);
		xmlWriter.write(doc);
		xmlWriter.close();
	}

	public void readFile(String filename) throws DocumentException {
		this.fileName = filename;
		map = new HashMap<ArrayList<String>, Integer>();
		content = new ArrayList<String[]>();
//		dbSet = new HashSet<String>();
//		langSet = new HashSet<String>();
		iniDbLangSet();
		File file = new File(filename);

		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		Element root = doc.getRootElement();

		@SuppressWarnings("unchecked")
		Iterator<Element> it = root.elementIterator();
		while (it.hasNext()) {
			Element e = it.next();
			this.add(e.attributeValue(HEADER_CODE[0]),
					e.attributeValue(HEADER_CODE[1]),
					e.attributeValue(HEADER_CODE[2]),
					e.attributeValue(HEADER_CODE[3]),
					e.attributeValue(HEADER_CODE[4]));
		}
	}

	public String getFileName() {
		return this.fileName;
	}
	public String[] getDbs(){
		String[] a = new String[dbSet.size()];
		ArrayList<String> al = new ArrayList<String>(dbSet);
		Collections.sort(al);
		int i = 0;
		for (String s:al){
			 a[i] = s;
			 ++i;
		}
		return a;
	}
	public String[] getLangs(){

		String[] a = new String[langSet.size()];
		ArrayList<String> al = new ArrayList<String>(langSet);
		Collections.sort(al);
		int i = 0;
		for (String s:al){
			 a[i] = s;
			 ++i;
		}
		return a;
	}
	public int size() {
		return this.content.size();
	}
	public void save(){
		try {
			this.saveToFile(this.fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private void delRow(int idx) {
		String[] v = content.get(idx);
		map.remove(tokey(v));
		content.remove(idx);
	}
	private void recreateContent(){
		
	}
}
