package util.parse;

import java.util.ArrayList;

import entity.UColumn;
import entity.UModel;
import entity.UReference;
import entity.UTable;
import entity.UView;


public class CAFun {

	public static String lowerFirstCharAndDeleteUnderline(String underlineDividStr){
		if (underlineDividStr ==null){
			return "";
		}
		String[] sa = underlineDividStr.split("_");
		if (sa.length ==0)
			return underlineDividStr;
		String rtn = sa[0].toLowerCase();
		for (int i=1;i<sa.length;++i){
			String s = sa[i];
			rtn += upperFirst(s);
		}
		return rtn;
	}

	public static String upperFirstCharAndDeleteUnderline(String underlineDividStr){
		if (underlineDividStr ==null){
			return "";
		}
		String[] sa = underlineDividStr.split("_");
		String rtn = "";
		for (int i=0;i<sa.length;++i){
			String s = sa[i];
			rtn += upperFirst(s);
		}
		return rtn;
	}

	
	public static String upperFirst(String str) {
		if (str != null && str != "" && str.length()!=0) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		}
		return str;
	}
	
	
	static public ArrayList<UReference> GetUReferences(String tablecode){
		if (global.GVar.gModel != null){
			return global.GVar.gModel.getRefs(tablecode);
		}
		return null;
	}
	public static UTable GetUTable(String tablecode) {
		if (global.GVar.gModel != null){
			if (global.GVar.gModel.tableMap != null)
				return global.GVar.gModel.tableMap.get(tablecode);
		}
		return null;
	}

	static public String GetTableName(String tablecode){
		UTable t = GetUTable(tablecode);
		if (t != null)
			return t.getName();
		return null;
	}
	
	public static String GetColRemark(UModel m, UTable t,	UColumn c) {
		String rtn = "";
		if (m == null||t==null||c==null)
			return "";
//		if (t.getPrimaryKey().contains(c))
//			rtn += "主键;";
		for (UColumn k:t.getPrimaryKey()){
			if (k.getCode().equals(c.getCode())){
				rtn += "主键;";
			}
		}
		ArrayList<UReference> refs = m.getRefs(t.getCode());
		if (refs != null){
			for (UReference r:refs){
				if (r.getColPairs().get(c.getCode()) != null){
					rtn += "外键（"+r.getParentTableCode()+":"+r.getColPairs().get(c.getCode())+"）;";
				}
			}
		}
		if (c.getComment() != ""){
			rtn += c.getComment()+";";
		}
		if (rtn != ""){
			rtn = rtn.substring(0, rtn.length()-1);
		}
		return rtn;
	}
	
	public static String GetColRemark(UModel m, UView v,	UColumn c) {
		String rtn = "";
		if (m == null||v==null||c==null)
			return "";
		if (c.getComment() != ""){
			rtn += c.getComment()+";";
		}
		if (rtn != ""){
			rtn = rtn.substring(0, rtn.length()-1);
		}
		return rtn;
	}
	
	static public String getRawDataType(String dbdt){
		int i;
		for (i=0;i<dbdt.length();++i){
			char c = dbdt.charAt(i);
			if (c == '(')
				break;
		}
		String type = dbdt.substring(0,i);
		return type;
	}
}
