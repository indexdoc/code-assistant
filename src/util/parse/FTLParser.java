package util.parse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import entity.Tpl;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FTLParser  extends Parser{
	
	public FTLParser(){
		super();
	}
	
	//1、对单表进行解析和模板化，TPL_TYPE_TABLE模式的模板
	//2、对模型进行解析和模板化，TPL_TYPE_MODEL模式的模板
	public String parse(Object o,Tpl tpl,String outpath){
		Template template=FTLParser.loadTplFile(tpl.getPath(),tpl.getName());
		if (template == null)
			return null;
		String outfilename = getOutFileName(o,tpl);
		if (outfilename == null)
			return null;
		Map<String,Object> root=new HashMap<String,Object>();
		if (tpl.getType() == Tpl.TPL_TYPE_MODEL)
			root.put("model", o);
		else if (tpl.getType() == Tpl.TPL_TYPE_TABLE)
			root.put("table", o);
		else
			return null;
		StringWriter sw=new StringWriter();
		try {
			template.process(root, sw);
			File f = new File(outpath+File.separator+outfilename);
			FileWriter fw = new FileWriter(f); 
			fw.write(sw.toString());
			fw.close();
		} catch (TemplateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return outfilename;
	}
	
	private static Template loadTplFile(String path,String filename){
		Configuration cfg=new Configuration();
		Template t = null;
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			t = cfg.getTemplate(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	private static String getOutFileName(Object o,Tpl tpl){
		StringTemplateLoader stl = new StringTemplateLoader();
		stl.putTemplate("NameRegular", tpl.getNameRegular());
        Configuration cfg = new Configuration();   
        cfg.setTemplateLoader(stl);   
        cfg.setDefaultEncoding("UTF-8");   
        
        try {
			Template template = cfg.getTemplate("NameRegular");
            StringWriter writer = new StringWriter();       
            template.process(o, writer);   
            return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}       
		return null;
	}
}
