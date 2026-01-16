package entity;

import java.util.ArrayList;

import global.GVar;
import util.parse.CAFun;
import util.parse.VTLParser;

public class UView {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
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
//	public ArrayList<UColumn> getPrimaryKey() {
//		return primaryKey;
//	}
//	public void addPrimaryKeyCol(String columncode) {
//		primaryKey.add(this.getColumnByCode(columncode));
//	}
	
	//将列按格式输出，并去掉最后一个分隔符
	public String getCols(String format,String separator){
		return VTLParser.formatTableColumns(this,format,separator);
	}

	//将列按格式输出，并去掉最后一个分隔符
	public String getColsExcept(String format,String separator,String exceptCols){
		return VTLParser.formatTableColumnsExcept(this,format,separator,exceptCols);
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
	/*
	public ArrayList<UReference> getRefs(){
		return CAFun.GetUReferences(this.code);
	}
	//适用于单一主键，不适合复合主键
	public UColumn getPrimaryColumn(){
		if (primaryKey.size() > 0){
			return primaryKey.get(0);
		}
		return null;
	}
	//适用于单一主键，不适合复合主键
	public UColumn getPrimaryColumn2(){
		if (primaryKey.size() > 1){
			return primaryKey.get(1);
		}
		return null;
	}
	*/
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
public UDomain getDomain() {
		return domain;
	}
	public void setDomain(UDomain domain) {
		this.domain = domain;
	}
	//	private ArrayList<UColumn> primaryKey = new ArrayList<UColumn>();
	private ArrayList<UColumn> columns = new ArrayList<UColumn>();
//	public void setPrimaryKey(ArrayList<UColumn> primaryKey) {
//		this.primaryKey = primaryKey;
//	}
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
