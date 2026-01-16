package util.parse;

import java.io.StringWriter;

import entity.Tpl;


public class Parser {
	
	private static int SAVE_SECONDS_PER_FILE = 5*60;
	private static int SAVE_SECONDS_PER_CODELINE = 60;
	
	
	private int fileNum = 0;
	private int codeNum = 0;
	
	public int getFileNum(){
		return fileNum;
	}
	public int getCodeNum(){
		return codeNum;
	}
	public int getSaveSeconds(){
		return fileNum * SAVE_SECONDS_PER_FILE+codeNum*SAVE_SECONDS_PER_CODELINE;
	}
	public String getStatistics(){
		int seconds = getSaveSeconds();
		String s = "生成文件个数："+fileNum +"\n" 
				+ "生成代码行数："+codeNum+"\n"
				+ "为您节省了“"+seconds/3600+"小时"+ (seconds%3600)/60+"分钟" +"”的编码时间！\n"
				+ "注：按文件5分钟，每行代码需1分钟的时间进行计算！";
		return s;
	}
	
	protected void stat(StringWriter sw){
		++fileNum;
		codeNum+=countCode(sw);
	}
	
	private int countCode(StringWriter sw){
		String fc = sw.toString();
		int num = 0;
		String[] lines= fc.split("\n");
		for (String l:lines){
			l=l.trim();
			if (!l.equals("")){
				++num;
			}
		}
		return num;
	}
	
	protected Parser(){
	}
	
	static public Parser CreateParser(){
		return new VTLParser();
//		FTLParser fp = new FTLParser();
//		String filename = tpl.getName();
//		if (filename == null)
//			return null;
//		int i = filename.lastIndexOf('.'); 
//		if (i==-1)
//			return null;
//		String subfix = filename.substring(i);
//		if (subfix.equalsIgnoreCase(Tpl.FREEMARKER_TEMPLATE_FILE)){
//			return new FTLParser();			
//		}
//		else if (subfix.equalsIgnoreCase(Tpl.VELOCITY_TEMPLATE_FILE))
//			return new VTLParser();	
//		return null;
	}
	
	//1、对单表进行解析和模板化，TPL_TYPE_TABLE模式的模板
	//2、对模型进行解析和模板化，TPL_TYPE_MODEL模式的模板
	public String parse(Object o,Tpl tpl,String outpath) throws Exception{
//		String filename = tpl.getName();
//		if (filename == null)
//			return null;
//		int i = filename.lastIndexOf('.'); 
//		if (i==-1)
//			return "";
//		String subfix = filename.substring(i);
//		if (subfix.equalsIgnoreCase(Tpl.FREEMARKER_TEMPLATE_FILE))
//			return FTLParser.parse(o, tpl, outpath);
//		else if (subfix.equalsIgnoreCase(Tpl.VELOCITY_TEMPLATE_FILE))
//			return VTLParser.parse(o, tpl, outpath);
		return null;
	}
}
