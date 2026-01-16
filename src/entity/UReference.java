package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import util.parse.CAFun;

public class UReference {
//	private String tableName;
//	private String tableCode;
//	private Map<String,UColumn> columns;
	
	private String name = "";
	private String code = "";
	private String parentTableCode;
	private String childTableCode;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public String getDisplayName(){
		UColumn fromcol = getFromColumn();
		if (fromcol.getName().substring(-1, 2).equalsIgnoreCase("ID")){
			UTable totable = getToTable();
			return totable.getName();
		}
		else {
			return fromcol.getName();
		}
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentTableCode() {
		return parentTableCode;
	}
	public void setParentTableCode(String parentTableCode) {
		this.parentTableCode = parentTableCode;
	}
	public String getChildTableCode() {
		return childTableCode;
	}
	public void setChildTableCode(String childTableCode) {
		this.childTableCode = childTableCode;
	}
	public Map<String, String> getColPairs() {
		return colPairs;
	}
	public void addColPair(String childColumnCode,String parentColumnCode) {
		colPairs.put(childColumnCode, parentColumnCode);
	}	
	
	public String getParentTableName(){
		return CAFun.GetTableName(this.parentTableCode);
	}
	public UTable getToTable(){
		return getParentTable();
	}
	public UTable getParentTable(){
		return CAFun.GetUTable(this.parentTableCode);
	}
	public UTable getFromTable(){
		return getChildTable();
	}
	public UTable getChildTable(){
		return CAFun.GetUTable(this.childTableCode);
	}

	private HashMap<String,String> colPairs = new HashMap<String,String>();
	public String getChildColumnCode(){
		Set<String> keyset = colPairs.keySet();
		String childcol = null;
		for (String s:keyset){
			childcol = s;
		}
		return childcol;
	}
	public String getParentColumnCode(){
		Set<String> keyset = colPairs.keySet();
		String childcol = null;
		for (String s:keyset){
			childcol = s;
		}
		return colPairs.get(childcol);
	}
	
	public UColumn getToColumn(){
		return getParentColumn(); 
	}
	public UColumn getChildColumn(){
		Object o[] = colPairs.keySet().toArray();
		String childcol = (String)o[0];
		return this.getChildTable().getColumnByCode(childcol);
	}
	public UColumn getFromColumn(){
		return getChildColumn(); 
	}
	public UColumn getParentColumn(){
		Object o[] =  colPairs.keySet().toArray();
		String childcol = (String)o[0];
		return this.getParentTable().getColumnByCode(colPairs.get(childcol));
	}
	public UColumn getChildColumn2(){
		Object o[] = colPairs.keySet().toArray();
		if (o.length < 2)
			return null;
		String childcol = (String)o[1];
		return this.getChildTable().getColumnByCode(childcol);
	}
	public UColumn getParentColumn2(){
		Object o[] = colPairs.keySet().toArray();
		if (o.length < 2)
			return null;
		String childcol = (String)o[1];
		return this.getParentTable().getColumnByCode(colPairs.get(childcol));
	}	
}
