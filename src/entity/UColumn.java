package entity;

import java.util.ArrayList;

import global.GVar;
import util.parse.CAFun;


public class UColumn {
	
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
	public String getType() {
		return type;
	}
	public String getDbType(){
		return dbType;
	}
	public String getComment(){
		return comment;
	}
	public void setComment(String comment){
		this.comment = comment;
	}
	public String getDict(){
		String comment = this.comment;
		if (comment==null){
			return null;
		}
		if (!comment.toUpperCase().startsWith("DICT"))
			return null;
		//字典默认均为单选
		String dictstr = comment.substring(5);
		//去掉回车换行符，空格
		dictstr = dictstr.replace("\n", "");
		dictstr = dictstr.replace("\r", "");
		dictstr = dictstr.replace("\t", "");
		dictstr = dictstr.replace(" ", "");
		//去掉单引号，双引号
		dictstr = dictstr.replace("\'", "");
		dictstr = dictstr.replace("\"", "");
		//去掉首尾的[]
		dictstr = dictstr.replace("[", "");
		dictstr = dictstr.replace("]", "");
		String[] strs = dictstr.split(",");
		String[] strs2 = new String[strs.length]; 
		for (int i = 0;i< strs.length; ++i){
			strs2[i] = "\"" + strs[i] + "\"";
		}
		String jsonstr = "[";
		for (String str:strs2){
			if (str.equals("\"\""))
				continue;
			jsonstr = jsonstr + str +",";
		}
		jsonstr = jsonstr.substring(0,jsonstr.length()-1)+"]";
		return jsonstr;
	}
	
	
	public String typeCvt(String dbms, String lang){
		TypeMap typemap = GVar.gWin.getTypeMap();
		return typemap.getLangDataType(dbms, this.type, lang);		
	}
	
	public String nullString(String nullStr, String notNullStr ){
		if (nullStatus.equals("null")){
			return nullStr;
		}
		else{
			return notNullStr;
		}
	}
	
	public boolean isForeignKey(){
		ArrayList<UReference> refs =  this.getTable().getMyRefs();
		for (UReference ref:refs){
			if (ref.getFromColumn().getCode().equals(this.code)){
				return true;
			}
		}
		return false;
	}
	public UTable refToTable(){
		ArrayList<UReference> refs =  this.getTable().getMyRefs();
		for (UReference ref:refs){
			if (ref.getFromColumn().getCode().equals(this.code)){
				return ref.getToTable();
			}
		}
		return null;
	}
	public UColumn refToColumn(){
		ArrayList<UReference> refs =  this.getTable().getMyRefs();
		for (UReference ref:refs){
			if (ref.getFromColumn().getCode().equals(this.code)){
				return ref.getToColumn();
			}
		}
		return null;
	}
	public String getDisplayName(){
		if (isForeignKey()){
			if (this.name.toUpperCase().endsWith("ID")){
				return this.name.substring(0, this.name.length()-2);
//				UTable totable = refToTable();
//				return totable.getName();
			}
		}
		return this.getName();
	}
	public void setType(String type) {
		this.dbType = type;
		if (type == null)
			type = "";
		this.type = CAFun.getRawDataType(type);
		this.limits=type.substring(this.type.length());
	}
	
	public void setLimits(String limits){
		this.limits = limits;
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
	
	public String getLimits() {
		return limits;
	}
	private UTable table = null;
	private String name="";
	private String code="";
	private String type="";
	private String dbType="";
	private String limits="";
	private String comment= "";
	private String nullStatus = "";
	private int index = -1;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getOrderNo() {
		return index+1;
	}
	public String getNullStatus() {
		return nullStatus;
	}
	public void setNullStatus(String nullStatus) {
		this.nullStatus = nullStatus;
	}
	public UTable getTable(){
		return table;
	}
	public void setTable(UTable table){
		this.table = table;
	}
}
