package main;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import global.GVar;
import util.Config;


public class Main {

	protected Shell shlPdmcoder;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Main window = new Main();
			global.GVar.isTrial = false;
			global.GVar.config = new Config(System.getProperty("user.dir")+File.separator+"default.cfg");
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlPdmcoder.setLocation(util.ShellUtil.locationCenter(shlPdmcoder));
		shlPdmcoder.setMaximized(true);
		shlPdmcoder.open();
		shlPdmcoder.layout();
		while (!shlPdmcoder.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlPdmcoder = new Shell();
		shlPdmcoder.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				global.GVar.config.save();
			}
		});
		shlPdmcoder.setImage(SWTResourceManager.getImage(Main.class, "/res/32.ico"));
		shlPdmcoder.setMinimumSize(new Point(650, 400));
		shlPdmcoder.setSize(650, 400);
		shlPdmcoder.setText("编码助理 —— 模型＋模板＝代码，强大、简单易用的代码生成器！");
		shlPdmcoder.setLayout(new FormLayout());
		
		Button btn_about = new Button(shlPdmcoder, SWT.CENTER);
		btn_about.setToolTipText("关于");
		btn_about.setText("关于");
		btn_about.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				About d = new About(e.display.getActiveShell());
				d.open();
			}
		});
		btn_about.setFont(SWTResourceManager.getFont("宋体", 8, SWT.NORMAL));
		btn_about.setGrayed(true);
		FormData fd_btn_about = new FormData();
		fd_btn_about.top = new FormAttachment(0);
		fd_btn_about.right = new FormAttachment(100, -2);
		btn_about.setLayoutData(fd_btn_about);
		
		Button btn_help = new Button(shlPdmcoder, SWT.CENTER);
		btn_help.setText("帮助");
		btn_help.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = System.getProperty("user.dir");
//				System.out.println(path);
				String cmd = "cmd.exe /c start " + path +File.separator+"UserGuide.chm";
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		FormData fd_btn_help = new FormData();
		fd_btn_help.right = new FormAttachment(btn_about);
		btn_help.setLayoutData(fd_btn_help);
		btn_help.setToolTipText("打开帮助文档");
		btn_help.setGrayed(true);
		btn_help.setFont(SWTResourceManager.getFont("宋体", 8, SWT.NORMAL));
		
		Win win = new Win(shlPdmcoder, SWT.NONE);
		win.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				
			}
		});
		GVar.gWin = win;
		FormData fd_win = new FormData();
		fd_win.top = new FormAttachment(0);
		fd_win.left = new FormAttachment(0);
		fd_win.bottom = new FormAttachment(100);
		fd_win.right = new FormAttachment(100);
		win.setLayoutData(fd_win);
		FormData fd_window = new FormData();
		fd_window.bottom = new FormAttachment(100);
		fd_window.right = new FormAttachment(100);
		fd_window.top = new FormAttachment(0);
		fd_window.left = new FormAttachment(0);
		GVar.gWin.setLayoutData(fd_window);
		
//		shlPdmcoder.set
//		win.forceFocus();
		win.setFocus();
//		win.moveAbove(null);
	}
}
