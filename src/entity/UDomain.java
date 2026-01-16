package entity;

import java.util.ArrayList;

public class UDomain {
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ArrayList<UTable> getTables() {
		return tables;
	}
	public void setTables(ArrayList<UTable> tables) {
		this.tables = tables;
	}
	public ArrayList<UView> getViews() {
		return views;
	}
	public void setViews(ArrayList<UView> views) {
		this.views = views;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String name;
	public String code;
	public ArrayList<UTable> tables;
	public ArrayList<UView> views;
	public String getName(){
		return name.substring(2);
	}
	public UDomain(String code,String name){
		this.code = code;
		this.name = name;
		this.tables = new ArrayList<UTable>();
		this.views = new ArrayList<UView>();
	}
}
