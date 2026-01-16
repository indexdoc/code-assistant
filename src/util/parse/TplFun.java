package util.parse;

import entity.TypeMap;
import global.GVar;

public class TplFun {

	public String lFormat(String code) {
		return CAFun.lowerFirstCharAndDeleteUnderline(code);
	}

	public String uFormat(String code) {
		return CAFun.upperFirstCharAndDeleteUnderline(code);
	}

	public String toLangDataType(String dbdt, String lang) {
		TypeMap typemap = GVar.gWin.getTypeMap();
		return typemap.getLangDataType(dbdt, lang);
	}

	public String toLangDataType(String dbms, String dbdt, String lang) {
		TypeMap typemap = GVar.gWin.getTypeMap();
		return typemap.getLangDataType(dbms, dbdt, lang);
	}

	public String toLangDT(String dbdt, String lang) {
		TypeMap typemap = GVar.gWin.getTypeMap();
		return typemap.getLangDataType(dbdt, lang);
	}

	public String toLangDT(String dbms, String dbdt, String lang) {
		TypeMap typemap = GVar.gWin.getTypeMap();
		return typemap.getLangDataType(dbms, dbdt, lang);
	}

	// 将字符串的首字母小写，其余字母保持不变
	public String lowerFirst(String str) {
		return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
	}

	// 将字符串的首字母大写，其余字母保持不变
	public String upperFirst(String str) {
		return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
	}

	// 将字符串改为小写
	public String lower(String str) {
		return str.toLowerCase();
	}

	// 将字符串改为大写
	public String upper(String str) {
		return str.toUpperCase();

	}

}
