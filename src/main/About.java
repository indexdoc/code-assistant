package main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Link;

import util.EnigmaUtil;

public class About extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public About(Shell parent) {
		super(parent, SWT.BORDER | SWT.CLOSE | SWT.APPLICATION_MODAL);
		setText("关于");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.setLocation(util.ShellUtil.locationCenter(this.getParent(), shell));
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.BORDER | SWT.CLOSE | SWT.APPLICATION_MODAL);
		shell.setSize(463, 311);
		shell.setText(getText());
		
		Label lb_describe = new Label(shell, SWT.WRAP);
		lb_describe.setFont(SWTResourceManager.getFont("Tahoma", 14, SWT.NORMAL));
		lb_describe.setText("编码助理（Code Assist） V2013.0001");
		lb_describe.setBounds(10, 10, 352, 29);

		Label lb_userName = new Label(shell, SWT.WRAP);
		lb_userName.setText("正式版！");
		lb_userName.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		lb_userName.setBounds(10, 45, 332, 21);
		Label lb_userKey = new Label(shell, SWT.WRAP);
		lb_userKey.setText("感谢您支持正版！");
		lb_userKey.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		lb_userKey.setBounds(10, 72, 371, 21);

//		String userName = EnigmaUtil.RegLoadKey_Name();
//		String userKey = EnigmaUtil.RegLoadKey_Key();
//		if (userName == null)
//			lb_userName.setText("试用版！");
//		else 
//			lb_userName.setText("授权给 "+userName+" 使用");
//		if (userKey == null)
//			lb_userKey.setText("系列号：无");
//		else
//			lb_userKey.setText("系列号："+userKey);

//		Label lb_name = new Label(shell, SWT.WRAP);
//		lb_name.setBounds(10, 101, 270, 33);
//		String str_describe = "强大，简单易用的代码生成器！\n模型＋模板＝代码！";
//		lb_name.setText(str_describe);		
		
		Label lb_good = new Label(shell, SWT.WRAP);
		String str_good="I'm not a framework ,but can support Any Framework !\r\nI'm not a develop language, but can support Any Language !\r\nI'm not a DataBase ,but can support Any Database !\r\nI'm not a Design Pattern, but you can use Any Design Pattern in template !\r\nI'm a tool, a powerful and flexible tool helping you leave copy and post operation away .\r\nBy using this tool, You can define everything, you can generate countless code in 1 Minute by just a button press .\r\nYes , I am Code Assitance !";
		lb_good.setText("数据模型+模板=代码\r\n简单，易用，功能强大的代码生成器\r\n代码复用的最优方式，你可以生成一个软件项目的所有文件\r\n支持数据模型设计工具PowerDesigner\r\n支持Oracle、MySQL、MS SqlServer等关系数据库\r\n支持生成任何编程语言");
		lb_good.setBounds(10, 96, 412, 107);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(10, 209, 60, 21);
		lblNewLabel.setText("产品主页：");
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("技术支持：");
		label.setBounds(263, 209, 60, 21);
		
		Link link = new Link(shell, SWT.NONE);
		link.setBounds(76, 209, 170, 21);
		link.setText("<a>http://www.codeasst.com</a>");
		
		Link link_1 = new Link(shell, 0);
		link_1.setText("<a>jooze@qq.com</a>");
		link_1.setBounds(329, 209, 118, 21);
		
		Label lblJooze = new Label(shell, SWT.NONE);
		lblJooze.setText("Jooze开发出品，版权所有，侵权必究！");
		lblJooze.setBounds(10, 238, 344, 21);
	}
}
