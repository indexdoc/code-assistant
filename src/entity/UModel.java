package entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UModel {
	public void setTableCnt(Integer tableCnt) {
		this.tableCnt = tableCnt;
	}
	public void setViewCnt(Integer viewCnt) {
		this.viewCnt = viewCnt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public Integer getTableCnt() {
		return tableCnt;
	}
	public Integer getViewCnt() {
		return viewCnt;
	}
	public void setTableCnt(int tableCnt) {
		this.tableCnt = tableCnt;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Map<String, UTable> getTableMap() {
		return tableMap;
	}

	public void setTableMap(Map<String, UTable> tables) {
		this.tableMap = tables;
	}

	public Collection<UTable> getTableList() {
		ArrayList<UTable> al = new ArrayList<UTable>();
		for(Object obj:tableMap.keySet()){
			al.add(tableMap.get(obj));
		}
		return al;
	}
	public Collection<UView> getViewList(){
		ArrayList<UView> al = new ArrayList<UView>();
		for(Object obj:viewMap.keySet()){
			al.add(viewMap.get(obj));
		}
		return al;
	}
	public Collection<UTable> getTables(){
		return getTableList();
	}
	public Collection<UView> getViews(){
		return getViewList();
	}
	public UTable getTable(String tcode){
		if (tableMap == null)
			return null;
		return tableMap.get(tcode);
	}
	public UView getView(String vcode){
		if (viewMap == null)
			return null;
		return viewMap.get(vcode);
	}
	public String name = "";
	public String code = "";
	public Integer tableCnt = 0;
	public Integer viewCnt = 0;
	public String fileName = "";
	public String filePath = "";
	public String url = "";

	public Map<String, UTable> tableMap = null;
	public Map<String,UView> viewMap= null;
	public ArrayList<UDomain> domains = new ArrayList<UDomain>();
	
	public ArrayList<UDomain> getDomains() {
		return domains;
	}
	public void setDomains(ArrayList<UDomain> domains) {
		this.domains = domains;
	}
	//通过表名得到表的引用
	private HashMap<String, ArrayList<UReference>> refMap = new HashMap<String, ArrayList<UReference>>();
	
	//所有选中的表
	public ArrayList<UTable> selectedTables = null;
	public ArrayList<UView> selectedViews = null;

	public ArrayList<UView> getSelectedViews() {
		return selectedViews;
	}
	public void setSelectedViews(ArrayList<UView> selectedViews) {
		this.selectedViews = selectedViews;
	}
	public ArrayList<UReference> getRefs(){
		ArrayList<UReference> refs = new ArrayList<UReference>();
		if (refMap == null)
			return null;
		for (ArrayList<UReference> al:refMap.values()){
			refs.addAll(al);
		}
		return refs;
	}
	
	public ArrayList<UReference> getRefs(String tablecode){
		return refMap.get(tablecode);
	}

	public void addRef(UReference r) {
		String childtablecode = r.getChildTableCode();
		ArrayList<UReference> aref = refMap.get(childtablecode);
		if (aref == null){
			aref = new ArrayList<UReference>();
			refMap.put(childtablecode, aref);
		}
		aref.add(r);
	}

	public ArrayList<UTable> getSelectedTables() {
		return selectedTables;
	}

	public void setSelectedTables(ArrayList<UTable> selectedTables) {
		this.selectedTables = selectedTables;
	}

	public String toString(){
		if (name.equals("")){
			return "Noname Model";
		}
		return name;
	}
}
