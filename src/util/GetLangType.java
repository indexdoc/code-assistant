package util;

import java.util.List;

import entity.TypeMap;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import global.GVar;

public class GetLangType implements TemplateMethodModel {
	// 参数为 数据库的字段类型，目标语言的数据类型
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		if (args.size() == 2) {
			String dbdt = (String) args.get(0);
			String lang = (String) args.get(1);
			dbdt = dbdt.toUpperCase();
			lang = lang.toUpperCase();
			TypeMap typemap = GVar.gWin.getTypeMap();
			return typemap.getLangDataType(dbdt, lang);
		}else if (args.size() == 3){
			String dbms = (String) args.get(0);
			String dbdt = (String) args.get(1);
			String lang = (String) args.get(2);
			dbdt = dbdt.toUpperCase();
			lang = lang.toUpperCase();
			TypeMap typemap = GVar.gWin.getTypeMap();
			return typemap.getLangDataType(dbms, dbdt, lang);
		}else if (args.size() == 1){
			return (String) args.get(0);
		}
		return "GetLangType Error";
	}
}
