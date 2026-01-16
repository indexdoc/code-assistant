package bizcompos;

import global.GVar;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import util.model.PdmUtil;
import util.model.XmlUtil;

//import org.eclipse.wb.swt.SWTResourceManager;

public class OpenModel extends Composite {

	private Label lb_filename;
	private Label lb_filepath;
	private Label lb_modelname;
	private Label lb_tablecnt;
	private Label lb_viewcnt;
	private Label lb_url;
	private Group grpModel;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public OpenModel(Composite parent, int style) {
		super(parent, style);
		FormLayout formLayout = new FormLayout();
		setLayout(formLayout);

		Group grppdm = new Group(this, SWT.NONE);
		grppdm.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD
				| SWT.ITALIC));
		grppdm.setLayout(new GridLayout(1, false));
		FormData fd_grppdm = new FormData();
		fd_grppdm.bottom = new FormAttachment(100, -10);
		fd_grppdm.right = new FormAttachment(0, 138);
		fd_grppdm.top = new FormAttachment(0, 15);
		fd_grppdm.left = new FormAttachment(0, 10);
		grppdm.setLayoutData(fd_grppdm);

		grpModel = new Group(this, SWT.NONE);
		grpModel.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD
				| SWT.ITALIC));
		grpModel.setText("\u6A21\u578B\u4FE1\u606F");
		FormData fd_grpModel = new FormData();
		fd_grpModel.bottom = new FormAttachment(grppdm, 0, SWT.BOTTOM);
		fd_grpModel.top = new FormAttachment(grppdm, 0, SWT.TOP);
		fd_grpModel.left = new FormAttachment(grppdm, 6);

		Button btnOpenFile = new Button(grppdm, SWT.NONE);
		btnOpenFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnOpenFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clear();
				Shell s = Display.getCurrent().getActiveShell();
				FileDialog dialog = new FileDialog(s, SWT.OPEN);
				// dialog.setFilterPath(System.getProperty("java.home"));
				dialog.setFilterExtensions(new String[] { "*.pdm" });
				dialog.setFilterNames(new String[] { "PDM 文件 (*.pdm)" });
				String filename = dialog.open();
				if (filename == null) {
					MessageDialog.openError(s, "打开文件错误！", "打开文件失败！");
					s.setCursor(new Cursor(null, SWT.NONE));
					return;
				}
				s.setCursor(new Cursor(null, SWT.CURSOR_WAIT));
				GVar.gModel = PdmUtil.OpenModel(filename);
				if (GVar.gModel == null) {
					MessageDialog.openError(s, "错误！",
							"打开PDM文件失败！\n请检查是否已经正确安装PowerDesinger16.1！");
					s.setCursor(new Cursor(null, SWT.NONE));
					return;
				}
				openSuccess();
				s.setCursor(new Cursor(null, SWT.NONE));
			}
		});
		btnOpenFile.setText("打开PDM文件");

		Button btnUseOpened = new Button(grppdm, SWT.NONE);
		GridData gd_btnUseOpened = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_btnUseOpened.widthHint = 112;
		btnUseOpened.setLayoutData(gd_btnUseOpened);
		btnUseOpened.setAlignment(SWT.LEFT);
		btnUseOpened.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clear();
				Shell s = Display.getCurrent().getActiveShell();
				s.setCursor(new Cursor(null, SWT.CURSOR_WAIT));
				GVar.gModel = PdmUtil.UseOpenedModel();
				if (GVar.gModel == null) {
					MessageDialog
							.openError(s, "错误！",
									"没有已打开的PDM文件！\n请检查是否已经正确安装PowerDesinger16.5并已打开PDM文件！");
					s.setCursor(new Cursor(null, SWT.NONE));
					return;
				}
				openSuccess();
				s.setCursor(new Cursor(null, SWT.NONE));
			}
		});
		btnUseOpened.setText("使用已打开的PDM");

		Button btn_openXML = new Button(grppdm, SWT.NONE);
		btn_openXML.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clear();
				Shell s = e.display.getActiveShell();
				String filename;
				// 选择文件名和路径
				FileDialog d = new FileDialog(s, SWT.OPEN);
				d.setText("打开数据模型XML文件！");
				d.setFilterNames(new String[] { "数据模型XML文件(*.model.xml)" });
				d.setFilterExtensions(new String[] { "*.model.xml" });
				filename = d.open();// 返回的全路径(路径+文件名)
				// 保存文件
				if (filename == null) {
					MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
					return;
				} else if (filename.equals("")) {
					MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
					return;
				}
				try {
					s.setCursor(new Cursor(null, SWT.CURSOR_WAIT));
					GVar.gModel = XmlUtil.openXML(filename);
					if (GVar.gModel == null) {
						MessageDialog
								.openError(s, "错误！", "打开文件失败，请检查文件格式是否正确！");
						return;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageDialog.openError(s, "错误！", "打开文件失败，请检查文件格式是否正确！");
					return;
				} finally {
					s.setCursor(new Cursor(null, SWT.NONE));
				}
				// MessageDialog.openInformation(s, "提示！", "数据模型导出成功！");
				openSuccess();
			}
		});
		btn_openXML.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btn_openXML.setText("打开XML文件");

		Button btn_openOracle = new Button(grppdm, SWT.NONE);
		btn_openOracle.setText("连接ORACLE");
		btn_openOracle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clear();
				Shell s = e.display.getActiveShell();
				// 连接数据库的对话框；
				DbmsDialog d = new DbmsDialog(getShell());
				d.open();
				// 获取数据
				GVar.gModel = d.model;
				if (GVar.gModel == null) {
					MessageDialog.openError(s, "错误！", "连接数据库失败！");
					return;
				}
				openSuccess();
			}
		});
		btn_openOracle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		Button btn_openMySQL = new Button(grppdm, SWT.NONE);
		GridData gd_btn_openMySQL = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btn_openMySQL.widthHint = 112;
		btn_openMySQL.setLayoutData(gd_btn_openMySQL);
		btn_openMySQL.setText("连接MySQL");

		Button btn_openSqlserver = new Button(grppdm, SWT.NONE);
		btn_openSqlserver.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btn_openSqlserver.setText("连接SqlServer");

		Button btnpostgresql = new Button(grppdm, SWT.NONE);
		btnpostgresql.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnpostgresql.setText("连接PostgreSQL");
		grpModel.setLayout(new FormLayout());
		fd_grpModel.right = new FormAttachment(100, -10);
		grpModel.setLayoutData(fd_grpModel);

		Label lb_modelname_head = new Label(grpModel, SWT.NONE);
		FormData fd_lb_modelname_head = new FormData();
		fd_lb_modelname_head.top = new FormAttachment(0, 15);
		fd_lb_modelname_head.left = new FormAttachment(0, 5);
		lb_modelname_head.setLayoutData(fd_lb_modelname_head);
		lb_modelname_head.setText("数据模型名称：");

		lb_modelname = new Label(grpModel, SWT.NONE);
		FormData fd_lb_modelname = new FormData();
		fd_lb_modelname.top = new FormAttachment(lb_modelname_head, 0, SWT.TOP);
		fd_lb_modelname.left = new FormAttachment(lb_modelname_head, 0);
		fd_lb_modelname.right = new FormAttachment(100, -6);
		lb_modelname.setLayoutData(fd_lb_modelname);
		lb_modelname.setText("无");
		lb_modelname.setFont(SWTResourceManager
				.getFont("Tahoma", 8, SWT.NORMAL));

		Label lb_tablecnt_head = new Label(grpModel, SWT.NONE);
		FormData fd_lb_tablecnt_head = new FormData();
		fd_lb_tablecnt_head.top = new FormAttachment(lb_modelname_head, 10);
		fd_lb_tablecnt_head.right = new FormAttachment(lb_modelname_head, 0,
				SWT.RIGHT);
		lb_tablecnt_head.setLayoutData(fd_lb_tablecnt_head);
		lb_tablecnt_head.setText("表数量：");

		lb_tablecnt = new Label(grpModel, SWT.NONE);
		FormData fd_lb_tablecnt = new FormData();
		fd_lb_tablecnt.top = new FormAttachment(lb_tablecnt_head, 0, SWT.TOP);
		fd_lb_tablecnt.left = new FormAttachment(lb_tablecnt_head, 0);
		fd_lb_tablecnt.right = new FormAttachment(100, -6);
		lb_tablecnt.setLayoutData(fd_lb_tablecnt);
		lb_tablecnt.setText("0");
		lb_tablecnt
				.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));

		Label lb_viewcnt_head = new Label(grpModel, SWT.NONE);
		FormData fd_lb_viewcnt_head = new FormData();
		fd_lb_viewcnt_head.top = new FormAttachment(lb_tablecnt_head, 10);
		fd_lb_viewcnt_head.right = new FormAttachment(lb_tablecnt_head, 0,
				SWT.RIGHT);
		lb_viewcnt_head.setLayoutData(fd_lb_viewcnt_head);
		lb_viewcnt_head.setText("视图数量：");

		lb_viewcnt = new Label(grpModel, SWT.NONE);
		FormData fd_lb_viewcnt = new FormData();
		fd_lb_viewcnt.top = new FormAttachment(lb_viewcnt_head, 0, SWT.TOP);
		fd_lb_viewcnt.left = new FormAttachment(lb_viewcnt_head, 0);
		fd_lb_viewcnt.right = new FormAttachment(100, -6);
		lb_viewcnt.setLayoutData(fd_lb_viewcnt);
		lb_viewcnt.setText("0");
		lb_viewcnt
				.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));
		
		Label lb_filename_head = new Label(grpModel, SWT.NONE);
		FormData fd_lb_filename_head = new FormData();
		fd_lb_filename_head.top = new FormAttachment(lb_viewcnt_head, 10);
		fd_lb_filename_head.right = new FormAttachment(lb_viewcnt_head, 0,
				SWT.RIGHT);
		lb_filename_head.setLayoutData(fd_lb_filename_head);
		lb_filename_head.setText("文件名：");

		lb_filename = new Label(grpModel, SWT.NONE);
		FormData fd_lb_filename = new FormData();
		fd_lb_filename.right = new FormAttachment(100, -6);
		fd_lb_filename.top = new FormAttachment(lb_filename_head, 0, SWT.TOP);
		fd_lb_filename.left = new FormAttachment(lb_filename_head, 0);
		lb_filename.setLayoutData(fd_lb_filename);
		lb_filename
				.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));
		lb_filename.setText("无");

		Label lb_path_head = new Label(grpModel, SWT.NONE);
		FormData fd_lb_path_head = new FormData();
		fd_lb_path_head.top = new FormAttachment(lb_filename_head, 10);
		fd_lb_path_head.right = new FormAttachment(lb_filename_head, 0,
				SWT.RIGHT);
		lb_path_head.setLayoutData(fd_lb_path_head);
		lb_path_head.setText("文件路径：");

		lb_filepath = new Label(grpModel, SWT.NONE);
		FormData fd_lb_filepath = new FormData();
		fd_lb_filepath.top = new FormAttachment(lb_path_head, 0, SWT.TOP);
		fd_lb_filepath.left = new FormAttachment(lb_path_head, 0);
		fd_lb_filepath.right = new FormAttachment(100, -6);

		lb_filepath.setLayoutData(fd_lb_filepath);
		lb_filepath.setText("无");
		lb_filepath
				.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));

		Label lb_url_head = new Label(grpModel, SWT.NONE);
		lb_url_head.setText("连接地址：");
		FormData fd_lb_url_head = new FormData();
		fd_lb_url_head.top = new FormAttachment(lb_path_head, 12);
		fd_lb_url_head.right = new FormAttachment(lb_path_head, 0, SWT.RIGHT);
		lb_url_head.setLayoutData(fd_lb_url_head);

		lb_url = new Label(grpModel, SWT.NONE);
		lb_url.setText("无");
		lb_url.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));
		FormData fd_lb_url = new FormData();
		fd_lb_url.top = new FormAttachment(lb_url_head, 0, SWT.TOP);
		fd_lb_url.left = new FormAttachment(lb_url_head, 0);
		fd_lb_url.right = new FormAttachment(100, -6);
		lb_url.setLayoutData(fd_lb_url);
	}

	private void openSuccess() {
		lb_filename.setText(GVar.gModel.fileName);
		lb_filepath.setText(GVar.gModel.filePath);
		lb_modelname.setText(GVar.gModel.name);
		lb_url.setText(GVar.gModel.url);
		lb_tablecnt.setText(GVar.gModel.tableCnt.toString());
		lb_viewcnt.setText(GVar.gModel.viewCnt.toString());
		GVar.gWin.freshState();
	}

	private void clear() {
		lb_filename.setText("无");
		lb_filepath.setText("无");
		lb_modelname.setText("无");
		lb_url.setText("无");
		lb_tablecnt.setText("0");
		lb_viewcnt.setText("0");
		GVar.gWin.freshState();
	}
	
	public boolean isReady(){
		if (GVar.gModel==null)
			return false;
		else if (GVar.gModel.getName().equals("")){
			return false;
		}
		return true;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
