package util.parse;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import entity.Tpl;
import util.FileUtil;

public class VTLParser extends Parser{
	
	public VTLParser(){
		super();
	}

	public String parse(Object o, Tpl tpl, String outpath)throws Exception {

		String dtstr = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
		String outfilename = getOutFileName(o, tpl);
		if (outfilename == null)
			return null ;

		// 获取模板路径,实例化模板上下文
		VelocityContext context = new VelocityContext();
		// 往上下文中添加参数
		if (tpl.getType().equals(Tpl.TPL_TYPE_MODEL))
			context.put("model", o);
		else if (tpl.getType().equals(Tpl.TPL_TYPE_TABLE))
			context.put("table", o);
		else if (tpl.getType().equals(Tpl.TPL_TYPE_VIEW))
			context.put("view", o);
		else
			return null;
		// 添加功能函数
		TplFun fun = new TplFun();
		context.put("fun", fun);
		//实例化StringWriter对象,将上下文与字符流进行合并到模板中
		StringWriter sw = new StringWriter();

//		FileReader fr = new FileReader(tpl.getPath() + File.separator
//				+ tpl.getName());
		String str = FileUtil.readFile(tpl.getPath() + File.separator
				+ tpl.getName());
		StringReader sr  = new StringReader(str);
		Velocity.evaluate(context, sw, tpl.getName() + "+" + o.toString() + "+"
				+ dtstr, sr);

		File f = new File(outpath + File.separator + outfilename);
		File path = f.getParentFile();
		if (!path.exists()){
			path.mkdirs();
		}
		FileWriter fw = new FileWriter(f);
		fw.write(sw.toString());
		stat(sw);
		fw.close();
		sw.close();
		sr.close();
		return outfilename;
	}
	
//	static public String formatTableColumns(UTable table, String format){
//		//format:${col.name},${col.code},${col.ucode},${col.lcode}
//        VelocityContext context = new VelocityContext();
//		TplFun fun = new TplFun();
//		context.put("fun",fun);
//		context.put("sep",File.separator);
//		context.put("table", table);
//		String reg = "#foreach($col in $table.columns) " + format+ "#end";
//		StringWriter sw = new StringWriter();
//		String dtstr = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
//		Velocity.evaluate(context, sw,  table.toString()+"+"+dtstr, reg);
//		String rtn = sw.toString().trim();
//		rtn = rtn.substring(0, rtn.length()-1);
//		return rtn;
//	}

	static public String formatTableColumns(Object tableOrView, String format,String separator){
		//format:${col.name},${col.code},${col.ucode},${col.lcode}
        VelocityContext context = new VelocityContext();
        String linesep = "" + '\n';
        separator = separator.replace("\\n",linesep);
       
		TplFun fun = new TplFun();
		context.put("fun",fun);
		context.put("sep",File.separator);
		context.put("table", tableOrView);
		String reg = "#foreach($col in $table.columns)"+ format+separator +"#end";
		StringWriter sw = new StringWriter();
		String dtstr = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
		Velocity.evaluate(context, sw,  tableOrView.toString()+"+"+dtstr, reg);  
		String rtn = sw.toString();
		rtn = rtn.substring(0, rtn.length()-separator.length());
		return rtn;
	}

	static public String formatTableColumnsExcept(Object tableOrView, String format,String separator,String exceptcols){
		//format:${col.name},${col.code},${col.ucode},${col.lcode}
		String[] ecols1 = exceptcols.split(",");
		ArrayList<String> alecol = new ArrayList<String>();
		for (String ecol:ecols1){
			alecol.add(ecol.trim());
		}
        VelocityContext context = new VelocityContext();
        String linesep = "" + '\n';
        separator = separator.replace("\\n",linesep);
       
		TplFun fun = new TplFun();
		context.put("fun",fun);
		context.put("sep",File.separator);
		context.put("table", tableOrView);
		String regexp = "";
		for (int i=0;i<alecol.size();++i ){
			if (i == 0)
				regexp += String.format("#if ($col.code==\"%s\")\n", alecol.get(i));
			else
				regexp += String.format("#elseif ($col.code==\"%s\")\n", alecol.get(i));
		}
		String reg = "#foreach($col in $table.columns)\n";
		if (alecol.size()>0){
			reg += regexp + "#else\n" + format+separator +"#end"+"\n#end";
		}else{
			reg += format+separator +"#end";
		}
		StringWriter sw = new StringWriter();
		String dtstr = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
		Velocity.evaluate(context, sw,  tableOrView.toString()+"+"+dtstr, reg);  
		String rtn = sw.toString();
		rtn = rtn.substring(0, rtn.length()-separator.length());
		return rtn;
	}
	
	private String getOutFileName(Object o,Tpl tpl){
        VelocityContext context = new VelocityContext();
		TplFun fun = new TplFun();
		context.put("fun",fun);
		context.put("sep",File.separator);
		if (tpl.getType().equals(Tpl.TPL_TYPE_MODEL))
			context.put("model", o);
		else if (tpl.getType().equals(Tpl.TPL_TYPE_TABLE))
			context.put("table", o);
		else if (tpl.getType().equals(Tpl.TPL_TYPE_VIEW))
			context.put("view", o);
		else
			return null;
		StringWriter sw = new StringWriter();
		String dtstr = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
		Velocity.evaluate(context, sw,  tpl.getName()+"+"+o.toString()+"+"+dtstr, tpl.getNameRegular());
		return sw.toString();
	}
}
