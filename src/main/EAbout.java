package main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Link;

public class EAbout extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EAbout(Shell parent) {
		super(parent, SWT.BORDER | SWT.CLOSE | SWT.APPLICATION_MODAL);
		setText("关于");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
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
		shell.setSize(389, 274);
		shell.setText(getText());
		
		Label lb_describe = new Label(shell, SWT.WRAP);
		lb_describe.setFont(SWTResourceManager.getFont("Tahoma", 14, SWT.NORMAL));
		lb_describe.setText("编码助理（Code Assist） V2013.0001");
		lb_describe.setBounds(10, 10, 352, 29);

		Label lblxxxx = new Label(shell, SWT.WRAP);
		lblxxxx.setText("授权给XXXX使用");
		lblxxxx.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		lblxxxx.setBounds(10, 45, 332, 21);
		
		Label lblxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = new Label(shell, SWT.WRAP);
		lblxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.setText("系列号：XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		lblxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		lblxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.setBounds(10, 72, 371, 21);

//		Label lb_name = new Label(shell, SWT.WRAP);
//		lb_name.setBounds(10, 101, 270, 33);
//		String str_describe = "强大，简单易用的代码生成器！\n模型＋模板＝代码！";
//		lb_name.setText(str_describe);		
		
		Label lb_good = new Label(shell, SWT.WRAP);
		String str_good="I'm not a framework ,but can support Any Framework !\r\nI'm not a develop language, but can support Any Language !\r\nI'm not a DataBase ,but can support Any Database !\r\nI'm not a Design Pattern, but you can use Any Design Pattern in template !\r\nI'm a tool, a powerful and flexible tool helping you leave copy and post operation away .\r\nBy using this tool, You can define everything, you can generate countless code in 1 Minute by just a button press .\r\nYes , I am Code Assitance !";
		lb_good.setText(str_good);
		lb_good.setBounds(10, 96, 363, 107);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(10, 209, 60, 13);
		lblNewLabel.setText("产品主页：");
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("技术支持：");
		label.setBounds(220, 209, 60, 13);
		
		Link link = new Link(shell, SWT.NONE);
		link.setBounds(76, 209, 138, 13);
		link.setText("<a>http://www.codeasst.com</a>");
		
		Link link_1 = new Link(shell, 0);
		link_1.setText("<a>jooze@qq.com</a>");
		link_1.setBounds(286, 209, 118, 13);
		
		Label lblJooze = new Label(shell, SWT.NONE);
		lblJooze.setText("Jooze开发出品，版权所有，侵权必究！");
		lblJooze.setBounds(10, 228, 344, 13);
		

	}
}
