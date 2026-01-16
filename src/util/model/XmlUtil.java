package util.model;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import entity.UColumn;
import entity.UModel;
import entity.UReference;
import entity.UTable;

public class XmlUtil {
	static public void saveAsXML(UModel model,String filename)throws Exception{
		File file = new File(filename);
		
		Document doc = DocumentHelper.createDocument();
		
		Element root = doc.addElement("Model");
		
		root.addAttribute("FileName", file.getName());
		root.addAttribute("FilePath", file.getParent());
		root.addAttribute("Name", model.name);
		root.addAttribute("TableCnt", model.tableCnt.toString());
		root.addAttribute("Url", model.url);
		
		for (UTable t:model.getTables()) {
			Element et = root.addElement("Table");
			et.addAttribute("Name",t.getName());
			et.addAttribute("Code",t.getCode());
			et.addAttribute("Comment",t.getComment());
			if (model.selectedTables != null){
				et.addAttribute("Checked",model.selectedTables.contains(t)?"true":"false");
			}else{
				et.addAttribute("Checked","false");				
			}
			for (UColumn c:t.getColumns()){
				Element ec = et.addElement("Column");
				ec.addAttribute("Name",c.getName());
				ec.addAttribute("Code",c.getCode());
				ec.addAttribute("Type",c.getType());
				ec.addAttribute("Limits",c.getLimits());
				ec.addAttribute("IsPrimaryKey",t.getPrimaryKey().contains(c)?"true":"false");
			}
//			for (UColumn c:t.getPrimaryKey()){
//				Element ec = et.addElement("PrimaryKey");
//				ec.addAttribute("Name",c.getName());
//				ec.addAttribute("Code",c.getCode());
//				ec.addAttribute("Type",c.getType());
//			}
		}
		for (UReference ref:model.getRefs()){
			Element er = root.addElement("Reference");
			er.addAttribute("Name",ref.getName()==null?"":ref.getName());
			er.addAttribute("Code",ref.getCode()==null?"":ref.getCode());
			er.addAttribute("ChildTableCode",ref.getChildTableCode());
			er.addAttribute("ParentTableCode",ref.getParentTableCode());
			for (String childcol:ref.getColPairs().keySet()){
				Element epair = er.addElement("ColumnMap");
				epair.addAttribute("ChildColumn", childcol);
				epair.addAttribute("ParentColumn", ref.getColPairs().get(childcol));
			}
		}
		Writer fileWriter = new FileWriter(filename);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8"); 
		XMLWriter xmlWriter = new XMLWriter(fileWriter,format);
		xmlWriter.write(doc);
		xmlWriter.close();
	}
	
	@SuppressWarnings("unchecked")
	static public UModel openXML(String filename)throws Exception{
		UModel m = new UModel();
		File file=new File(filename);
		SAXReader reader=new SAXReader();
		Document doc=reader.read(file);
		Element root=doc.getRootElement();
		
		m.name = root.attributeValue("Name");
		m.filePath = file.getParent();
		m.fileName = file.getName();
		m.tableCnt = new Integer(root.attributeValue("TableCnt"));
		m.url = root.attributeValue("Url");

		HashMap<String,UTable> tabmap = new HashMap<String,UTable>();
		ArrayList<UTable> selectedtables = new ArrayList<UTable>();
		m.tableMap = tabmap;
		m.selectedTables = selectedtables;
		
		Iterator<Element> itt=root.elementIterator("Table");
		while (itt.hasNext()){
			Element e = itt.next();
			UTable t = new UTable();
			t.setName(e.attributeValue("Name"));
			t.setCode(e.attributeValue("Code"));
			t.setComment(e.attributeValue("Comment"));
			Iterator<Element> itc = e.elementIterator("Column");
			ArrayList<UColumn> cols = new ArrayList<UColumn>();
			ArrayList<UColumn> primarykey = new ArrayList<UColumn>();
			while(itc.hasNext()){
				UColumn c = new UColumn();
				Element ec = itc.next();
				c.setCode(ec.attributeValue("Code"));
				c.setType(ec.attributeValue("Type"));
				c.setName(ec.attributeValue("Name"));
				c.setLimits(ec.attributeValue("Limits",""));
				cols.add(c);
				String ispkey = ec.attributeValue("IsPrimaryKey");
				if (ispkey!=null && ispkey.equals("true")){
					primarykey.add(c);
				}
			}
			t.setColumns(cols);
			t.setPrimaryKey(primarykey);
			tabmap.put(t.getCode(), t);
			String selected = e.attributeValue("IsSelected");
			if (selected!=null && selected.equals("true")){
				selectedtables.add(t);
			}
		}

		Iterator<Element> itr=root.elementIterator("Reference");
		while (itr.hasNext()){
			Element e = itr.next();
			UReference r = new UReference();
			r.setName(e.attributeValue("Name",""));
			r.setCode(e.attributeValue("Code",""));
			r.setChildTableCode(e.attributeValue("ChildTableCode"));
			r.setParentTableCode(e.attributeValue("ParentTableCode"));

			for (Object o:e.elements("ColumnMap")){
				Element er = (Element)o;
				r.addColPair(er.attributeValue("ChildColumn"), er.attributeValue("ParentColumn"));
			}
			m.addRef(r);
		}
		return m;
	}
}
