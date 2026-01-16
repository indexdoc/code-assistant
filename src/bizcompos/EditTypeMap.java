package bizcompos;

import java.io.File;
import java.io.IOException;

import main.Win;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;
import entity.TypeMap;
import global.GVar;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class EditTypeMap extends Composite {
	private KTable kt_typeMap;
	public TypeMap typeMap;
	private TypeMapKTableDetail ktd_typeMap;

	private Text tx_fileName;
	private Combo cb_lang;
	private Combo cb_db;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EditTypeMap(Composite parent, int style) {
		super(parent, style);
		typeMap = new TypeMap();
		ktd_typeMap = new TypeMapKTableDetail(typeMap);
		setLayout(new FormLayout());

		Button btn_new = new Button(this, SWT.NONE);
		btn_new.setText("新建");
		btn_new.setEnabled(false);
		btn_new.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				Shell s = Display.getCurrent().getActiveShell();
				String filename;
				// 选择文件名和路径
				FileDialog d = new FileDialog(s, SWT.SAVE);
				d.setText("新建数据类型映射文件！");
				d.setFilterNames(new String[] { "数据类型映射文件(*.typemap)" });
				d.setFilterExtensions(new String[] { "*.typemap" });
				filename = d.open();// 返回的全路径(路径+文件名)
				// 保存文件
				if (filename == null) {
					MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
					return;
				}
				try {
					typeMap = new TypeMap();
					typeMap.saveToFile(filename);
				} catch (IOException e1) {
					MessageDialog.openError(s, "错误！", "保存文件失败，请检查后重试！");
					e1.printStackTrace();
					return;
				}
				MessageDialog.openInformation(s, "提示！", "新建文件成功！");
				tx_fileName.setText(filename);
				ktd_typeMap.setContent(typeMap);
				kt_typeMap.setModel(ktd_typeMap);
				cb_db.select(-1);
				cb_lang.select(-1);
			}
		});
		FormData fd_btn_new = new FormData();
		fd_btn_new.left = new FormAttachment(0, 5);
		fd_btn_new.top = new FormAttachment(0, 10);
		btn_new.setLayoutData(fd_btn_new);
		btn_new.setToolTipText("新建映射关系配置文件");

		Button btn_open = new Button(this, SWT.NONE);
		btn_open.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Shell s = Display.getCurrent().getActiveShell();

				FileDialog d = new FileDialog(s, SWT.OPEN);
				d.setText("打开已有的数据类型映射文件！");
				d.setFilterNames(new String[] { "数据类型映射文件(*.typemap)" });
				d.setFilterExtensions(new String[] { "*.typemap" });
				String filename = d.open();// 返回的全路径(路径+文件名)
				if (filename == null) {
					MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
					return;
				} else if (filename.equals("")) {
					MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
					return;
				}
				typeMap = new TypeMap();
				try {
					typeMap.readFile(filename);
				} catch (Exception e1) {
					MessageDialog.openError(s, "错误！", "读取文件错误！");
					e1.printStackTrace();
					return;
				}
				tx_fileName.setText(filename);
				MessageDialog.openInformation(s, "提示！", "打开数据类型映射文件成功！");
				ktd_typeMap.setContent(typeMap);
				kt_typeMap.setModel(ktd_typeMap);
				cb_db.select(0);
				cb_lang.select(0);
				GVar.gWin.freshState();
			}
		});
		FormData fd_btn_open = new FormData();
		fd_btn_open.left = new FormAttachment(btn_new, 6);
		fd_btn_open.top = new FormAttachment(0, 10);
		btn_open.setLayoutData(fd_btn_open);
		btn_open.setText("打开");
		btn_open.setToolTipText("打开已有的数据类型映射");

		Button btn_save = new Button(this, SWT.NONE);
		btn_save.setEnabled(false);
		btn_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = Display.getCurrent().getActiveShell();
				String filename = typeMap.getFileName();
				if (filename == null) {
					// 选择文件名和路径
					FileDialog d = new FileDialog(s, SWT.SAVE);
					d.setText("保存数据类型映射文件！");

					d.setFilterNames(new String[] { "数据类型映射文件(*.typemap)" });
					d.setFilterExtensions(new String[] { "*.typemap" });
					filename = d.open();// 返回的全路径(路径+文件名)
					// 保存文件
					if (filename == null) {
						MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
						return;
					} else if (filename.equals("")) {
						MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
						return;
					}
				}

				try {
					typeMap.saveToFile(filename);
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageDialog.openError(s, "错误！", "保存文件失败！请检查文件是否可以写入！");
				}
				MessageDialog.openInformation(s, "提示！", "数据类型映射保存成功！");

				tx_fileName.setText(filename);
			}
		});
		FormData fd_btn_save = new FormData();
		fd_btn_save.left = new FormAttachment(btn_open, 6);
		fd_btn_save.top = new FormAttachment(0, 10);
		btn_save.setLayoutData(fd_btn_save);
		btn_save.setText("保存");
		btn_save.setToolTipText("保存数据类型映射");

		tx_fileName = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
//		tx_fileName.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				if (typeMap.size() > 0)
//					GVar.gWin.setState(Win.TYPE_MAP_SELECTED);
//				else
//					GVar.gWin.setState(Win.TPL_SELECTED);
//			}
//		});
		tx_fileName
				.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.NORMAL));
		FormData fd_tx_fileName = new FormData();
		fd_tx_fileName.bottom = new FormAttachment(btn_save, 0, SWT.BOTTOM);
		fd_tx_fileName.top = new FormAttachment(btn_save, 0, SWT.TOP);
		fd_tx_fileName.left = new FormAttachment(btn_save, 6);
		fd_tx_fileName.right = new FormAttachment(100, -19);
		tx_fileName.setLayoutData(fd_tx_fileName);
		tx_fileName.setText("无数据映射配置文件");

		Group gp_filter = new Group(this, SWT.NONE);
		gp_filter.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.ITALIC));
		gp_filter.setText("数据筛选");
		gp_filter.setLayout(new GridLayout(5, false));
		FormData fd_gp_filter = new FormData();
		fd_gp_filter.top = new FormAttachment(btn_new);
		fd_gp_filter.right = new FormAttachment(0, 445);
		fd_gp_filter.left = new FormAttachment(0, 5);
		gp_filter.setLayoutData(fd_gp_filter);

		Label lb_db = new Label(gp_filter, SWT.NONE);
		lb_db.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lb_db.setText("数据库");

		cb_db = new Combo(gp_filter, SWT.READ_ONLY);
		cb_db.setItems(new String[] {});
		cb_db.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ktd_typeMap.filter(cb_db.getText(),cb_lang.getText());
				kt_typeMap.setModel(ktd_typeMap);
			}
		});
		cb_db.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String[] dbs = typeMap.getDbs();
				String[] items = new String[dbs.length+1];
				items[0]="ALL";
				for (int i=0;i<dbs.length;++i){
					items[i+1]= dbs[i];
				}
				cb_db.setItems(items);
			}
		});
		GridData gd_cb_db = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_cb_db.widthHint = 140;
		gd_cb_db.minimumWidth = 50;
		cb_db.setLayoutData(gd_cb_db);
		cb_db.setText("选择数据库");

		Label lb_sep = new Label(gp_filter, SWT.SEPARATOR);
		GridData gd_lb_sep = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				1);
		gd_lb_sep.heightHint = 30;
		lb_sep.setLayoutData(gd_lb_sep);

		Label lb_lang = new Label(gp_filter, SWT.NONE);
		lb_lang.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lb_lang.setText("语言");

		cb_lang = new Combo(gp_filter, SWT.READ_ONLY);
		cb_lang.setItems(new String[] {});
		cb_lang.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ktd_typeMap.filter(cb_db.getText(),cb_lang.getText());
				kt_typeMap.setModel(ktd_typeMap);
			}
		});
		cb_lang.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String[] langs = typeMap.getLangs();
				String[] items = new String[langs.length+1];
				items[0]="ALL";
				for (int i=0;i<langs.length;++i){
					items[i+1]= langs[i];
				}
				cb_lang.setItems(items);			
			}
		});
		cb_lang.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		
		kt_typeMap = new KTable(this, SWT.BORDER | SWTX.AUTO_SCROLL //|SWT.FULL_SELECTION 
				|SWTX.FILL_WITH_LASTCOL | SWTX.EDIT_ON_KEY |SWTX.AUTO_SCROLL);
		FormData fd_kt_typeMap = new FormData();
		fd_kt_typeMap.top = new FormAttachment(gp_filter);
		fd_kt_typeMap.bottom = new FormAttachment(100, -30);
		fd_kt_typeMap.right = new FormAttachment(100, -5);
		fd_kt_typeMap.left = new FormAttachment(btn_new, 0, SWT.LEFT);
		kt_typeMap.setLayoutData(fd_kt_typeMap);
		kt_typeMap.setModel(ktd_typeMap);
		
		Button btn_addRow = new Button(this, SWT.NONE);
		btn_addRow.setText("新增映射");
		btn_addRow.setEnabled(false);
		btn_addRow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ktd_typeMap.addNullRow();
				kt_typeMap.setModel(ktd_typeMap);
				kt_typeMap.setSelection(1, typeMap.size()+1, true);
				GVar.gWin.freshState();
			}
		});
		FormData fd_btn_addRow = new FormData();
		fd_btn_addRow.left = new FormAttachment(btn_new, 5, SWT.LEFT);
		fd_btn_addRow.top = new FormAttachment(kt_typeMap, 3);
		btn_addRow.setLayoutData(fd_btn_addRow);
		
		Button btn_delRow = new Button(this, SWT.NONE);
		btn_delRow.setEnabled(false);
		btn_delRow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = Display.getCurrent().getActiveShell();
				Point ao[] = kt_typeMap.getCellSelection() ;
				if (ao.length == 0 ){
					MessageDialog.openInformation(s, "提示", "没有选中的条目！");
					return;
				}
				boolean b = MessageDialog.openQuestion(s, "提示", "您选中了第"+ao[0].y+"行记录，确定删除？");
				if(!b){
					return;
				}
//				typeMap.delRow(ao[0].y-1);
				ktd_typeMap.delRow(ao[0].y);
				kt_typeMap.setModel(ktd_typeMap);
//				kt_typeMap.setSelection(ao[0].x, ao[0].y-1, true);
				kt_typeMap.setSelection(ao[0].x, ao[0].y-1, true);
				GVar.gWin.freshState();
			}
		});
		btn_delRow.setText("删除选中映射");
		FormData fd_btn_delRow = new FormData();
		fd_btn_delRow.left = new FormAttachment(btn_addRow, 10);
		fd_btn_delRow.top = new FormAttachment(kt_typeMap, 3);
		btn_delRow.setLayoutData(fd_btn_delRow);
		loadDefaultCfg();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	private void loadDefaultCfg(){
		//在当前目录查找
		String curpath = System.getProperty("user.dir");
		String filename = curpath + File.separator + "default.typemap";
		File f = new File(filename);
		if (!f.exists())
			return;
		typeMap = new TypeMap();
		try {
			typeMap.readFile(filename);
		} catch (Exception e1) {
//			MessageDialog.openError(s, "错误！", "读取文件错误！");
			e1.printStackTrace();
			return;
		}
		tx_fileName.setText(filename);
		ktd_typeMap.setContent(typeMap);
		kt_typeMap.setModel(ktd_typeMap);
//		GVar.gWin.freshState();
	}
	public boolean isReady(){
		return typeMap.size()>0?true:false;
	}
}
