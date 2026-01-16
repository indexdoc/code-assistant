package bizcompos;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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

import util.FileUtil;

import entity.Tpl;

public class TplForm extends Composite {
	private Text tx_filename;
	private Text tx_outfilenamerule;
	public Text tx_filepath;
	private StyledText stx_content;
	private StyledText stx_remark;

	private Tpl tpl = null;

	public static String EDIT = "EDIT";
	public static String NEW = "NEW";

	private String state = EDIT;
	private String workPath;
	protected Combo cb_type;

	public String getState() {
		return state;
	}

	private void setState(String state) {
		this.state = state;
	}

	public Tpl getTpl() {
		return tpl;
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param t
	 * @param workPath
	 * @param state
	 */
	public TplForm(Composite parent, int style, String state, String workpath, Tpl t) {
		super(parent, style);
		setLayout(new FormLayout());
		this.state = state;
		this.workPath = workpath;
		this.tpl = t;
		Button btn_imp = new Button(this, SWT.NONE);
		btn_imp.setText("导入");
		btn_imp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = Display.getCurrent().getActiveShell();
				tpl = null;
				// String path = tx_filepath.getText();
				// 选择保存文件的路径
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				fd.setFilterNames(new String[] {
						"VELOCITY模板文件(*" + Tpl.VELOCITY_TEMPLATE_FILE + ")"/* ,"FREEMARKER模板文件(*.ftl)" */ });
				fd.setFilterExtensions(new String[] { "*" + Tpl.VELOCITY_TEMPLATE_FILE /* ,"*.ftl" */ });
				String filename = fd.open();
				if (filename == null) {
					MessageDialog.openError(shell, "错误", "请选择正确的文件路径");
					return;
				}
				File f = new File(filename);
				String name = f.getName();
				// if
				// (name.substring(name.length()-4).equalsIgnoreCase(Tpl.VELOCITY_TEMPLATE_FILE)){
				// name = name.substring(0,name.length()-1-4);
				// }
				// if(tpl.getType().equals(Tpl.TPL_TYPE_TABLE)){
				// cb_type.select(0);
				// }else if (tpl.getType().equals(Tpl.TPL_TYPE_MODEL)){
				// cb_type.select(1);
				// }else {
				// cb_type.select(-1);
				// }
				tx_filename.setText(name);
				try {
					String str = Tpl.readContent(filename);
					stx_content.setText(str);
					// 如果文件不在此目录，则拷贝到此目录中
					if (!f.getPath().equals(workPath + File.separator + name)) {
						// @TODO 需判断是否需要覆盖原来的文件
						FileUtil.copyFile(f, new File(workPath + File.separator + name));
					}
				} catch (IOException e1) {
					MessageDialog.openError(shell, "错误", "读取文件错误");
					e1.printStackTrace();
				}
			}
		});
		FormData fd_btn_imp = new FormData();
		fd_btn_imp.top = new FormAttachment(0, 10);
		fd_btn_imp.left = new FormAttachment(0, 10);
		btn_imp.setLayoutData(fd_btn_imp);

		Button btn_save = new Button(this, SWT.NONE);
		FormData fd_btn_save = new FormData();
		fd_btn_save.top = new FormAttachment(0, 10);
		fd_btn_save.left = new FormAttachment(btn_imp, 10);
		btn_save.setLayoutData(fd_btn_save);
		btn_save.setText("保存");
		btn_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = Display.getCurrent().getActiveShell();
				if (tx_filename.getText().equals("")) {
					MessageDialog.openError(shell, "错误", "请输入有效的模板名！");
					return;
				}
				if (tx_outfilenamerule.getText().equals("")) {
					MessageDialog.openError(shell, "错误", "请输入有效的模板输出文件名规则！");
					return;
				}
				if (cb_type.getSelectionIndex() == -1) {
					MessageDialog.openError(shell, "错误", "请选择模板类型！");
					return;
				}
				if (stx_content.getText().equals("")) {
					MessageDialog.openError(shell, "错误", "模板内容为空！");
				}
				tpl = null;
				String path = tx_filepath.getText();
				if (path.equals("")) {
					// 选择保存文件的路径
					String savename = tx_filename.getText();
					FileDialog fd = new FileDialog(shell, SWT.OPEN);
					fd.setFilterNames(new String[] { savename });
					fd.setFilterExtensions(new String[] { savename });
					fd.setFileName(savename);
					String filename = fd.open();// 返回的全路径(路径+文件名)
					if (filename == null) {
						MessageDialog.openError(shell, "错误", "请选择正确的文件路径");
						return;
					}
					File f = new File(filename);
					path = (new File(f.getAbsolutePath())).getParent();
					tx_filepath.setText(path);
				}
				// 保存文件
				try {
					tpl = new Tpl();
					tpl.setChecked(true);
					String filename = tx_filename.getText();
					if (filename.length() <= 4)
						filename = filename + Tpl.VELOCITY_TEMPLATE_FILE;
					else if (!filename.substring(filename.length() -4).equals(Tpl.VELOCITY_TEMPLATE_FILE))
						filename = filename + Tpl.VELOCITY_TEMPLATE_FILE;
					tx_filename.setText(filename);
					tpl.setName(filename);
					tpl.setPath(tx_filepath.getText());
					tpl.setNameRegular(tx_outfilenamerule.getText());
					tpl.setRemark(stx_remark.getText());
					tpl.saveContent(stx_content.getText());
					if (cb_type.getSelectionIndex() == 0) {
						tpl.setType(Tpl.TPL_TYPE_MODEL);
					} else if (cb_type.getSelectionIndex() == 1) {
						tpl.setType(Tpl.TPL_TYPE_TABLE);
					} else if (cb_type.getSelectionIndex() == 2) {
						tpl.setType(Tpl.TPL_TYPE_VIEW);
					}
				} catch (IOException e1) {
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "文件保存错误", "文件保存错误，请检查文件路径和文件名是否合法");
					e1.printStackTrace();
					return;
				}
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "提示", "保存文件成功");
			}
		});

		Button btn_recover = new Button(this, SWT.NONE);
		btn_recover.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tpl == null) {
					tx_filename.setText("");
					tx_filepath.setText(workPath);// 后续需要修改
					tx_outfilenamerule.setText("");
					stx_remark.setText("");
					stx_content.setText("");
					return;
				}
				tx_filename.setText(tpl.getName());
				tx_filepath.setText(workPath);// 后续需要修改
				tx_outfilenamerule.setText(tpl.getNameRegular());
				stx_remark.setText(tpl.getRemark());
				// 读取原始文件
				try {
					stx_content.setText("");
					stx_content.setText(tpl.getContent());
				} catch (IOException e1) {
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "文件保存错误", "文件保存错误，请检查文件路径和文件名是否合法");
					e1.printStackTrace();
					return;
				}
			}
		});
		FormData fd_btn_recover = new FormData();
		fd_btn_recover.left = new FormAttachment(btn_save, 10);
		fd_btn_recover.top = new FormAttachment(0, 10);
		btn_recover.setLayoutData(fd_btn_recover);
		btn_recover.setText("复原");

		Group gp_main = new Group(this, SWT.NONE);
		gp_main.setLayout(new FormLayout());
		FormData fd_gp_main = new FormData();
		fd_gp_main.top = new FormAttachment(btn_save);
		fd_gp_main.bottom = new FormAttachment(100, -10);
		fd_gp_main.right = new FormAttachment(35, 50);
		fd_gp_main.left = new FormAttachment(0, 10);
		gp_main.setLayoutData(fd_gp_main);

		Label lb_name = new Label(gp_main, SWT.NONE);
		FormData fd_lb_name = new FormData();
		fd_lb_name.top = new FormAttachment(0, 5);
		fd_lb_name.left = new FormAttachment(0, 5);
		lb_name.setLayoutData(fd_lb_name);
		lb_name.setText("模板名：");

		tx_filename = new Text(gp_main, SWT.BORDER);
		tx_filename.setToolTipText(" 模板文件名以" + Tpl.VELOCITY_TEMPLATE_FILE + "结尾，可以加子路径");
		tx_filename.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));
		FormData fd_tx_filename = new FormData();
		fd_tx_filename.left = new FormAttachment(lb_name);
		fd_tx_filename.right = new FormAttachment(100, -5);
		fd_tx_filename.top = new FormAttachment(0, 2);
		tx_filename.setLayoutData(fd_tx_filename);
		tx_filename.setText("");

		// Label lb_subfix = new Label(gp_main, SWT.NONE);
		// lb_subfix.setText(Tpl.VELOCITY_TEMPLATE_FILE);
		// FormData fd_lb_subfix = new FormData();
		// fd_lb_subfix.left = new FormAttachment(tx_filename, 0);
		// fd_lb_subfix.bottom = new FormAttachment(lb_name, 0, SWT.BOTTOM);
		// lb_subfix.setLayoutData(fd_lb_subfix);

		Label lb_type = new Label(gp_main, SWT.NONE);
		lb_type.setText("模板类型：");
		FormData fd_lb_type = new FormData();
		fd_lb_type.left = new FormAttachment(0, 5);
		fd_lb_type.top = new FormAttachment(lb_name, 10);
		lb_type.setLayoutData(fd_lb_type);

		cb_type = new Combo(gp_main, SWT.NONE);
		cb_type.setToolTipText("TPL_TYPE_TABLE：每个表生成一个文件。\r\nTABLE_LIST：每个模型生成一个文件。");
		cb_type.setItems(new String[] {"TPL_TYPE_MODEL", "TPL_TYPE_TABLE", "TPL_TYPE_VIEW"});
		FormData fd_cb_type = new FormData();
		fd_cb_type.left = new FormAttachment(lb_type);
		fd_cb_type.top = new FormAttachment(lb_name, 5);
		fd_cb_type.right = new FormAttachment(100, -5);
		cb_type.setLayoutData(fd_cb_type);

		Label lb_filepath = new Label(gp_main, SWT.NONE);
		lb_filepath.setToolTipText("由模板集文件所在路径确定");
		lb_filepath.setText("模板文件路径：");
		FormData fd_lb_filepath = new FormData();
		fd_lb_filepath.top = new FormAttachment(lb_type, 10);
		fd_lb_filepath.left = new FormAttachment(0, 5);
		lb_filepath.setLayoutData(fd_lb_filepath);

		tx_filepath = new Text(gp_main, SWT.BORDER);
		tx_filepath.setToolTipText("由模板集文件所在路径确定");
		tx_filepath.setEditable(false);
		if (workPath == null)
			workPath = "";
		tx_filepath.setText(workPath);
		tx_filepath.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));
		FormData fd_tx_filepath = new FormData();
		fd_tx_filepath.left = new FormAttachment(lb_filepath);
		fd_tx_filepath.top = new FormAttachment(lb_type, 10);
		fd_tx_filepath.right = new FormAttachment(100, -5);
		tx_filepath.setLayoutData(fd_tx_filepath);

		Label lb_nameRegular = new Label(gp_main, SWT.NONE);
		FormData fd_lb_nameRegular = new FormData();
		fd_lb_nameRegular.top = new FormAttachment(lb_filepath, 10);
		fd_lb_nameRegular.left = new FormAttachment(0, 5);
		lb_nameRegular.setLayoutData(fd_lb_nameRegular);
		lb_nameRegular.setText("生成文件命名：");

		tx_outfilenamerule = new Text(gp_main, SWT.BORDER);
		FormData fd_tx_outfilenamerule = new FormData();
		fd_tx_outfilenamerule.left = new FormAttachment(lb_nameRegular);
		fd_tx_outfilenamerule.top = new FormAttachment(lb_filepath, 10);
		fd_tx_outfilenamerule.right = new FormAttachment(100, -5);
		tx_outfilenamerule.setLayoutData(fd_tx_outfilenamerule);
		tx_outfilenamerule.setText("");

		Label lb_remark = new Label(gp_main, SWT.NONE);
		FormData fd_lb_remark = new FormData();
		fd_lb_remark.top = new FormAttachment(lb_nameRegular, 10);
		fd_lb_remark.left = new FormAttachment(0, 5);
		fd_lb_remark.bottom = new FormAttachment(100, -5);
		lb_remark.setLayoutData(fd_lb_remark);
		lb_remark.setText("备注：");

		stx_remark = new StyledText(gp_main, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		FormData fd_stx_remark = new FormData();
		fd_stx_remark.top = new FormAttachment(lb_nameRegular, 10);
		fd_stx_remark.left = new FormAttachment(lb_remark, 5);
		fd_stx_remark.bottom = new FormAttachment(100, -5);
		fd_stx_remark.right = new FormAttachment(100, -5);
		stx_remark.setLayoutData(fd_stx_remark);

		Group gp_content = new Group(this, SWT.NONE);
		gp_content.setText("文件内容");
		gp_content.setLayout(new FormLayout());
		FormData fd_gp_content = new FormData();
		fd_gp_content.left = new FormAttachment(gp_main, 10);
		fd_gp_content.right = new FormAttachment(100, -10);
		fd_gp_content.top = new FormAttachment(0, 10);
		fd_gp_content.bottom = new FormAttachment(100, -10);
		gp_content.setLayoutData(fd_gp_content);

		stx_content = new StyledText(gp_content, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		FormData fd_stx_content = new FormData();
		fd_stx_content.bottom = new FormAttachment(100, -5);
		fd_stx_content.top = new FormAttachment(0, 5);
		fd_stx_content.right = new FormAttachment(100, -5);
		fd_stx_content.left = new FormAttachment(0, 5);
		stx_content.setLayoutData(fd_stx_content);

		this.tx_filepath.setText(workPath);
		if (tpl != null) {
			String str = tpl.getName();
			// if
			// (str.substring(str.length()-4).equalsIgnoreCase(Tpl.VELOCITY_TEMPLATE_FILE)){
			// str = str.substring(0, str.length()-4);
			// }
			tx_filename.setText(str);
			if (tpl.getType().equals(Tpl.TPL_TYPE_MODEL)) {
				cb_type.select(0);
			} else if (tpl.getType().equals(Tpl.TPL_TYPE_TABLE)) {
				cb_type.select(1);
			}else if (tpl.getType().equals(Tpl.TPL_TYPE_VIEW)) {
				cb_type.select(2);
			}
			else {
				cb_type.select(-1);
			}
			tx_outfilenamerule.setText(tpl.getNameRegular());
			stx_remark.setText(tpl.getRemark());
			try {
				stx_content.setText(tpl.getContent());
			} catch (IOException e1) {
				MessageDialog.openError(this.getShell(), "错误！", "模板文件打开失败！");
				e1.printStackTrace();
			}
		}
		if (state.equals(TplForm.EDIT)) {
			tx_filename.setEnabled(false);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
