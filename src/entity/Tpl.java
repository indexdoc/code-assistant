package entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import util.FileUtil;

public class Tpl {
	
	//这个模板对每张表都生成一个文件
	static public String TPL_TYPE_TABLE = "TPL_TYPE_TABLE";
	//这个模板对每个视图都生成一个文件
	static public String TPL_TYPE_VIEW = "TPL_TYPE_VIEW";
	//这个模板对一个模型只生成一个文件
	static public String TPL_TYPE_MODEL = "TPL_TYPE_MODEL";

	static public String FREEMARKER_TEMPLATE_FILE=".ftl";
	static public String VELOCITY_TEMPLATE_FILE=".vtl";

	//模板名
	private String name;
	//模板类型，"TPL_TYPE_TABLE"、"TPL_TYPE_MODEL"、"TPL_TYPE_VIEW";
	private String type; 
	//模板文件路径
	private String path;
	//模板文件名规则
	private String nameRegular;
	//是否选择
	private boolean checked;	
	//模板内容
	private String content;
	//备注
	private String remark;
	//顺序
	private int order;
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getNameRegular() {
		return nameRegular;
	}
	public void setNameRegular(String nameRegular) {
		this.nameRegular = nameRegular;
	}
	public String getContent() throws IOException{
//		if (content != null)
//			return content;
		this.content = readContent(this.path + File.separator + this.name);
		return this.content;
	}
	
	static public String readContent(String filename) throws IOException{
		File f = new File(filename);
//		FileInputStream fi = new FileInputStream(f);
//		int filesize = fi.available();
//		byte[] bytes = new byte[filesize+1024];
//		int len = fi.read(bytes);
//		String str = new String(bytes,0,len);
//		fi.close();
//		FileReader fr = new FileReader(f);
		String charset = FileUtil.getCharset(f);
//		System.out.println(charset);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),charset));  
		StringBuffer buf = new StringBuffer();
		String s;
		while ((s = br.readLine() )!=null) {
			buf.append(s);
			buf.append("\n");
         }
		br.close();
		return buf.toString();
	}
	
	public void saveContent(String content)throws IOException{
		File f = new File(this.path+File.separator+this.name);
		FileOutputStream fo = new FileOutputStream(f);
		OutputStreamWriter osw  =   new  OutputStreamWriter(fo,  "UTF-8" );
		osw.write(content);
		this.content = content;
		osw.close();
		fo.close();
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
