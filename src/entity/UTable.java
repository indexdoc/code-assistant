package entity;

import java.util.ArrayList;
import java.util.HashMap;

import global.GVar;
import util.parse.CAFun;
import util.parse.Parser;
import util.parse.VTLParser;

public class UTable {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
//	//将列按格式输出，并去掉最后一个分隔符
//	public String getCols(String format){
//		return VTLParser.formatTableColumns(this,format);
//	}

	//将列按格式输出，并去掉最后一个分隔符
	public String getCols(String format,String separator){
		return VTLParser.formatTableColumns(this,format,separator);
	}

	//将列按格式输出，并去掉最后一个分隔符
	public String getColsExcept(String format,String separator,String exceptCols){
		return VTLParser.formatTableColumnsExcept(this,format,separator,exceptCols);
	}
	
	public String getUCode(){
		return CAFun.upperFirstCharAndDeleteUnderline(code);
	}
	public String getLCode(){
		return CAFun.lowerFirstCharAndDeleteUnderline(code);
	}
	public String getUcode(){
		return CAFun.upperFirstCharAndDeleteUnderline(code);
	}
	public String getLcode(){
		return CAFun.lowerFirstCharAndDeleteUnderline(code);
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public ArrayList<UColumn> getPrimaryKey() {
		return primaryKey;
	}
	public void addPrimaryKeyCol(String columncode) {
		primaryKey.add(this.getColumnByCode(columncode));
	}
	public ArrayList<UColumn> getColumns() {
		return columns;
	}
	public void addColumn(UColumn col) {
		this.columns.add(col);
	}
	
	public String getCodeUpperFirstCharAndDeleteUnderline(){
		return CAFun.upperFirstCharAndDeleteUnderline(code);
	}
	public String getUFormatCode(){
		return CAFun.upperFirstCharAndDeleteUnderline(code);
	}
	public String getCodeLowerFirstCharAndDeleteUnderline(){
		return CAFun.lowerFirstCharAndDeleteUnderline(code);
	}
	public String getLFormatCode(){
		return CAFun.lowerFirstCharAndDeleteUnderline(code);
	}
	public ArrayList<UReference> getRefs(){
		return CAFun.GetUReferences(this.code);
	}
	
	//取非主键非外键，code里带name，字段类型为字符串型的column
	public UColumn getNameColumn(){
		for (UColumn col:this.columns){//取标注有namecolumn的字段
			if (col.getComment().toUpperCase().equals("NAMECOLUMN"))
				return col;
		}
		for (UColumn col:this.columns){
			if (col == this.getIdcol())
				continue;
			boolean isref = false;
			for (UReference ref :this.getParentRefs()){
				if (col == ref.getChildColumn()){
					isref = true;
					break;
				}
			}
			if (col.getCode().equalsIgnoreCase("deleted")
				|| col.getCode().equalsIgnoreCase("status")
				|| col.getCode().equalsIgnoreCase("state")
				){
				continue;
			}
			if (isref == false && col.getCode().toLowerCase().contains("name") 
					&& (col.getDbType().toLowerCase().contains("varchar")
						||col.getDbType().toLowerCase().contains("text")
						||col.getDbType().toLowerCase().contains("string")
						)
				){
				return col;
			}
		}
		return this.getIdcol();
	}
	
	//引用此表的外键
	public ArrayList<UReference> getOtherRefs(){
		return getChildRefs();
	}
	public ArrayList<UReference> getChildRefs(){
		ArrayList<UReference> refs = GVar.gModel.getRefs();
		ArrayList<UReference> childrefs = new ArrayList<UReference>();
		for (UReference ref : refs){
			if (ref.getParentTableCode().equals(this.code)){
				childrefs.add(ref);
			}
		}
		return childrefs;
	}
	
	//此表的外键
	public ArrayList<UReference> getMyRefs(){
		return getParentRefs();
	}
	public ArrayList<UReference> getParentRefs(){
		ArrayList<UReference> refs = GVar.gModel.getRefs();
		ArrayList<UReference> parentrefs = new ArrayList<UReference>();
		for (UReference ref : refs){
			if (ref.getChildTableCode().equals(this.code)){
				parentrefs.add(ref);
			}
		}
		return parentrefs;
	}
	
	public UDomain getDomain() {
		return domain;
	}
	public void setDomain(UDomain domain) {
		this.domain = domain;
	}
	//获取多对多的外键
	//子表的 1、字段，2、子表，3、子表的其它非NULL外键对应的表
	//column,childtable,xtables
	public HashMap<UReference,ArrayList<UReference>> getX2xRefs(){
		HashMap<UReference,ArrayList<UReference>> x2xrefs = new HashMap<UReference,ArrayList<UReference>>();
		ArrayList<UReference> childrefs = this.getChildRefs();
		for (UReference xref : childrefs){
			ArrayList<UReference> prefs = xref.getChildTable().getParentRefs();
			ArrayList<UReference> toxrefs = new ArrayList<UReference>();
			for (UReference pref: prefs){
				if (pref.getChildColumn().getNullStatus().equals("not null") && pref != xref){
					toxrefs.add(pref);
				}
				if (toxrefs.size() > 0){
					x2xrefs.put(xref,toxrefs);
				}
			}
		}
		return x2xrefs;
	}
		
	//适用于单一主键，不适合复合主键
	public UColumn getPrimaryColumn(){
		if (primaryKey.size() > 0){
			return primaryKey.get(0);
		}
		return null;
	}
	public UColumn getIdcol(){
		return this.getPrimaryColumn();
	}
	
	//适用于单一主键，不适合复合主键
	public UColumn getPrimaryColumn2(){
		if (primaryKey.size() > 1){
			return primaryKey.get(1);
		}
		return null;
	}
	
	public UColumn getColumnByCode(String code){
		UColumn rtn = null;
		for (UColumn col:columns){
			if (col.getCode().equals(code)){
				rtn = col;
				break;
			}
		}
		return rtn;
	}
	
	public String toString(){
		return code;
	}
	private String name;
	private String code;
	private String comment;
	public UDomain domain;

	
	private ArrayList<UColumn> primaryKey = new ArrayList<UColumn>();
	private ArrayList<UColumn> columns = new ArrayList<UColumn>();
	public void setPrimaryKey(ArrayList<UColumn> primaryKey) {
		this.primaryKey = primaryKey;
	}
	public void setColumns(ArrayList<UColumn> columns) {
		this.columns = columns;
	}
	
	public UModel getModel(){
		return GVar.gModel;
	}	
	
	public String getModelName(){
		return GVar.gModel.name;
	}
	public String getModelCode(){
		return GVar.gModel.code;
	}
	
	public String getModelUCode(){
		return CAFun.upperFirstCharAndDeleteUnderline(GVar.gModel.code);		
	}
	public String getModelLCode(){
		return CAFun.lowerFirstCharAndDeleteUnderline(GVar.gModel.code);		
	}
	public String getModelUcode(){
		return CAFun.upperFirstCharAndDeleteUnderline(GVar.gModel.code);		
	}
	public String getModelLcode(){
		return CAFun.lowerFirstCharAndDeleteUnderline(GVar.gModel.code);		
	}
}
