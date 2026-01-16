package bizcompos;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import entity.Tpl;
import entity.TplSet;
import entity.UModel;
import entity.UTable;
import entity.UView;
import global.GVar;
import util.DateUtil;
import util.FileUtil;
import util.parse.Parser;

public class ToCode extends Composite {
	private Text tx_path;
	private Table t_list;
	private StyledText stx_log;
	private StyledText stx_content;
	
//	private StringBuffer logbuf = new StringBuffer();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ToCode(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
//		this.setSize(650, 400);

		Group gp_path = new Group(this, SWT.NONE);
		gp_path.setLayout(new FormLayout());
		FormData fd_gp_path = new FormData();
//		fd_gp_path.width = 440;
//		fd_gp_path.height = 50;
		fd_gp_path.top = new FormAttachment(0);
		fd_gp_path.left = new FormAttachment(0, 10);
		fd_gp_path.right = new FormAttachment(60, 90);
		fd_gp_path.bottom = new FormAttachment(0, 50);
		gp_path.setLayoutData(fd_gp_path);
		
		Label lb_path = new Label(gp_path, SWT.NONE);
		FormData fd_lb_path = new FormData();
		fd_lb_path.top = new FormAttachment(0, 10);
		fd_lb_path.left = new FormAttachment(0, 5);
		lb_path.setLayoutData(fd_lb_path);
		lb_path.setText("生成路径：");
		
		tx_path = new Text(gp_path, SWT.BORDER);
		tx_path.setEditable(false);
		FormData fd_tx_path = new FormData();
		fd_tx_path.left = new FormAttachment(lb_path);
		fd_tx_path.right = new FormAttachment(100, -40);
		fd_tx_path.top = new FormAttachment(0, 7);
		tx_path.setLayoutData(fd_tx_path);
		
		Button btn_path = new Button(gp_path, SWT.NONE);
		btn_path.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog d = new DirectoryDialog(getShell(), SWT.NONE);
				d.setText("选择输出文件的目录！");
				if (!tx_path.getText().equals(""))
					d.setFilterPath(tx_path.getText());
				String path = d.open();// 返回的全路径(路径+文件名)
				if (path == null) {
					MessageDialog.openError(getShell(), "错误！", "请选择有效的目录！");
					return;
				}
				tx_path.setText(path);
				GVar.gTplSet.setOutPath(path);
				GVar.gTplSet.save();
			}
		});
		FormData fd_btn_path = new FormData();
		fd_btn_path.left = new FormAttachment(tx_path, 5);
		fd_btn_path.top = new FormAttachment(0, 5);
		btn_path.setLayoutData(fd_btn_path);
		btn_path.setText("...");
		
		Group gp_list = new Group(this, SWT.NONE);
		gp_list.setText("生成文件列表(双击显示文件内容)");
		gp_list.setLayout(new FormLayout());
		FormData fd_gp_list = new FormData();
		fd_gp_list.right = new FormAttachment(40);
		fd_gp_list.left = new FormAttachment(gp_path,0,SWT.LEFT);
		fd_gp_list.top = new FormAttachment(gp_path, 5);
		fd_gp_list.bottom = new FormAttachment(100, -5);
		gp_list.setLayoutData(fd_gp_list);
		
		t_list = new Table(gp_list, SWT.BORDER | SWT.FULL_SELECTION);
		t_list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem[] items = t_list.getSelection();
				if (items.length == 0)
					return;
				TableItem item = items[0];
				String filename = item.getText(2);
				try {
					File f = new File(tx_path.getText() + File.separator + GVar.gModel.getCode() + File.separator + filename);
					FileInputStream fi = new FileInputStream(f);
					int filesize = fi.available();
					byte[] bytes = new byte[filesize+1024];
					int len = fi.read(bytes);
					String str = new String(bytes,0,len);
					fi.close();
					stx_content.setText(str);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		FormData fd_t_list = new FormData();
		fd_t_list.top = new FormAttachment(0);
		fd_t_list.bottom = new FormAttachment(100);
		fd_t_list.right = new FormAttachment(100);
		fd_t_list.left = new FormAttachment(0);
		t_list.setLayoutData(fd_t_list);
		t_list.setHeaderVisible(true);
		t_list.setLinesVisible(true);
		
		TableColumn tc_tablename = new TableColumn(t_list, SWT.NONE);
		tc_tablename.setWidth(100);
		tc_tablename.setText("对象名");
		
		TableColumn tc_tplname = new TableColumn(t_list, SWT.NONE);
		tc_tplname.setWidth(180);
		tc_tplname.setText("模板名");

		TableColumn tc_filename = new TableColumn(t_list, SWT.NONE);
		tc_filename.setWidth(200);
		tc_filename.setText("文件名");
		
		Group gp_content = new Group(this, SWT.NONE);
		gp_content.setText("文件内容");
		gp_content.setLayout(new FormLayout());
		FormData fd_gp_content = new FormData();
		fd_gp_content.bottom = new FormAttachment(70);
		fd_gp_content.right = new FormAttachment(100, -5);
		fd_gp_content.left = new FormAttachment(gp_list, 5);
		fd_gp_content.top = new FormAttachment(gp_path, 6);
		gp_content.setLayoutData(fd_gp_content);
		
		Group gp_log = new Group(this, SWT.NONE);
		gp_log.setText("文件生成日志");
		gp_log.setLayout(new FormLayout());
		FormData fd_gp_log = new FormData();
		fd_gp_log.bottom = new FormAttachment(100, -5);
		fd_gp_log.right = new FormAttachment(100, -5);
		fd_gp_log.left = new FormAttachment(gp_list, 5);
		fd_gp_log.top = new FormAttachment(gp_content, 6);
		gp_log.setLayoutData(fd_gp_log);

		stx_content = new StyledText(gp_content, SWT.H_SCROLL|SWT.V_SCROLL|SWT.BORDER);
		stx_content.setAlwaysShowScrollBars(false);
		stx_content.setEditable(false);
		FormData fd_stx_content = new FormData();
		fd_stx_content.bottom = new FormAttachment(100,0);
		fd_stx_content.top = new FormAttachment(0,0);
		fd_stx_content.right = new FormAttachment(100,0);
		fd_stx_content.left = new FormAttachment(0, 0);
		stx_content.setLayoutData(fd_stx_content);
		
		stx_log = new StyledText(gp_log,SWT.H_SCROLL|SWT.V_SCROLL|SWT.BORDER);
		stx_log.setAlwaysShowScrollBars(false);
		stx_log.setEditable(false);
		FormData fd_stx_log = new FormData();
		fd_stx_log.bottom = new FormAttachment(100,0);
		fd_stx_log.right = new FormAttachment(100,0);
		fd_stx_log.left = new FormAttachment(0,0);
		fd_stx_log.top = new FormAttachment(0,0);
		stx_log.setLayoutData(fd_stx_log);
		
		Button btn_do = new Button(this, SWT.NONE);
		btn_do.setText("生成文件");
		btn_do.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stx_content.setText("");
				Object o = null;
				Tpl t = null;
				try {
					if (tx_path.getText().equals("")){
						MessageDialog.openInformation(e.display.getActiveShell(), "错误！", "请先选择生成文件的输出路径！");
						return;
					}
					String filepath = tx_path.getText();
					File path = new File(tx_path.getText());
					if (!path.isDirectory()){
						MessageDialog.openInformation(e.display.getActiveShell(), "错误！", "输出路径不存在，请重新选择输出路径！");
						return;
					}
					File modelpath = new File(tx_path.getText() + File.separator + GVar.gModel.getCode());
					FileUtil.clearDir(modelpath);
					t_list.removeAll();
					//得到模板列表
					TplSet tplset = GVar.gTplSet;
					String outpath = tplset.getOutPath() + File.separator + GVar.gModel.getCode();;
					ArrayList<UTable> tables = GVar.gWin.getSelectedTables();
					ArrayList<UView> views = GVar.gWin.getSelectedViews();

					UModel model = GVar.gModel;
					Parser parser = Parser.CreateParser();
					int x = Integer.MAX_VALUE;
				//  20150101  	1420041600000
				//  20160101  	1451577600000
				//  20170101  	1483200000000
				//  20180101  	1514736000000
				//  20190101  	1546272000000
				//  20200101  	1577808000000
				//  20210101  	1609430400000
				//  20220101  	1640966400000
				//  20230101  	1672502400000
				//  20240101  	1704038400000
				//  20250101  	1735660800000
				//  20260101  	1767196800000
//					if (GVar.isTrial || System.currentTimeMillis() > 1767196800000L){
//						x = 3;
//					}
					Boolean a;
					
					for(Tpl tpl:tplset.getSelectedTpls())
					{
						t = tpl;
						if (tpl.getType().equals(Tpl.TPL_TYPE_MODEL)){
							if (--x <0 )
								break;
							o = model;
							String outfilename = parser.parse(model, tpl, outpath);
							TableItem item = new TableItem(t_list,SWT.NONE);
							if (outfilename == null){
								item.setText(new String[]{model.getName(),tpl.getName(),"生成失败！"});
								item.setForeground(e.display.getSystemColor( SWT.COLOR_RED));
								log("模型("+model.getName()+")+模板("+tpl.getName()+")生成文件("+tpl.getNameRegular()+")失败！");
							}else{
								item.setText(new String[]{model.getName(),tpl.getName(),outfilename});								
								log("模型("+model.getName()+")+模板("+tpl.getName()+")生成文件("+outfilename+")成功！");
							}
						}
						else if (tpl.getType().equals(Tpl.TPL_TYPE_TABLE)){
							for (UTable table:tables){
								if (--x <0 )
									break;
								o = table;
								String outfilename=parser.parse(table, tpl, outpath);
								TableItem item = new TableItem(t_list,SWT.NONE);
								if (outfilename == null){
									item.setText(new String[]{table.getCode(),tpl.getName(),"生成失败！"});
									item.setForeground(e.display.getSystemColor( SWT.COLOR_RED));
									log("表("+table.getCode()+")+模板("+tpl.getName()+")生成文件("+tpl.getNameRegular()+")失败！");
								}else{
									item.setText(new String[]{table.getCode(),tpl.getName(),outfilename});
									log("表("+table.getCode()+")+模板("+tpl.getName()+")生成文件("+outfilename+")成功！");
								}
							}
						}else if (tpl.getType().equals(Tpl.TPL_TYPE_VIEW)){
							for (UView view:views){
								if (--x <0 )
									break;
								o = view;
								String outfilename=parser.parse(view, tpl, outpath);
								TableItem item = new TableItem(t_list,SWT.NONE);
								if (outfilename == null){
									item.setText(new String[]{view.getCode(),tpl.getName(),"生成失败！"});
									item.setForeground(e.display.getSystemColor( SWT.COLOR_RED));
									log("视图("+view.getCode()+")+模板("+tpl.getName()+")生成文件("+tpl.getNameRegular()+")失败！");
								}else{
									item.setText(new String[]{view.getCode(),tpl.getName(),outfilename});
									log("视图("+view.getCode()+")+模板("+tpl.getName()+")生成文件("+outfilename+")成功！");
								}
							}
						}
					}
					String stat = parser.getStatistics();
					if (x >=0 ){//授权用户
						MessageDialog.openInformation(getShell(), "代码生成结束！", stat);
					}else{//非授权用户
						MessageDialog.openInformation(getShell(), "代码生成结束（非授权用户仅能生成3个文件）！", stat);
					}
					log("代码生成结束！");
					log(stat);
				} catch (Exception e1) {
					e1.printStackTrace();
					log(e1.getMessage());
					TableItem item = new TableItem(t_list,SWT.NONE);
					if (o == null) o = "null";
					if (t == null) {t = new Tpl();t.setName("null");}
					item.setText(new String[]{o.toString(),t.getName(),"生成失败！"});
					item.setForeground(e.display.getSystemColor( SWT.COLOR_RED));
				}
			}
		});
		btn_do.setFont(SWTResourceManager.getFont("Tahoma", 11, SWT.BOLD));
		FormData fd_btn_do = new FormData();
		fd_btn_do.top = new FormAttachment(0, 15);
		fd_btn_do.left = new FormAttachment(gp_path, 8);
		btn_do.setLayoutData(fd_btn_do);
		
		refresh();
	}
	
	public void refresh(){
		if (GVar.gTplSet != null){
			if (GVar.gTplSet.getOutPath() != null)
				tx_path.setText(GVar.gTplSet.getOutPath());
		}
	}

//	private String line1 =null;
//	private String line2 =null;
//	private String line3 =null;
	private void log(String str){
		if (stx_log.getText().equals("")){
			stx_log.append(DateUtil.nowStr("yyyy-MM-dd HH:mm:ss"));
		}
		stx_log.append("\n");
		stx_log.append(str);
		stx_log.setTopIndex(Integer.MAX_VALUE);
//		logbuf.append(str);
//		logbuf.append("\n");
//		if (line1==null){
//			line1=str;
//		}else if (line2==null){
//			line2=str;
//		}else if (line3==null){
//			line3=str;
//		}else {
//			line1=line2;
//			line2=line3;
//			line3=str;
//		}
//		stx_log.setText(line1+"\n"+line2+"\n"+line3);
//		stx_log.setTopIndex(Integer.MAX_VALUE);
//		MessageDialog.openInformation(getShell(), "", "");
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
//	private void logend(String str){
//		stx_log.append("\n");
//		stx_log.append(str);
////		logbuf.append(str);
////		logbuf.append("\n");
////		stx_log.setText(logbuf.toString());
////		stx_log.setTopIndex(Integer.MAX_VALUE);
//	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
