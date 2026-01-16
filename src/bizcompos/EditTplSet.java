package bizcompos;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import main.Win;

import org.dom4j.DocumentException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import entity.Tpl;
import entity.TplSet;
import entity.UTable;
import global.GVar;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.CheckStateChangedEvent;

public class EditTplSet extends Composite {
	private Table t_template;
	private CheckboxTableViewer tv_template;
	// private TplSet tplSet;
	private Button btn_addTpl;

	private String workPath;
	private Text tx_setFileName;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EditTplSet(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		Label lb_tplSet = new Label(this, SWT.NONE);
		lb_tplSet.setText("模板集：");
		lb_tplSet.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.BOLD));
		FormData fd_lb_tplSet = new FormData();
		fd_lb_tplSet.top = new FormAttachment(0, 15);
		fd_lb_tplSet.left = new FormAttachment(0, 10);
		lb_tplSet.setLayoutData(fd_lb_tplSet);

		// 新建模板集
		Button btn_newTplSet = new Button(this, SWT.NONE);
		btn_newTplSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = Display.getCurrent().getActiveShell();
				t_template.removeAll();
				TplSet tplSet = new TplSet();
				GVar.gTplSet = tplSet;
				String filename;
				// 选择文件名和路径
				FileDialog d = new FileDialog(s, SWT.SAVE);
				d.setText("新建模板集文件！");
				d.setFilterNames(new String[] { "模板集文件(*.tplset)" });
				d.setFilterExtensions(new String[] { "*.tplset" });
				filename = d.open();// 返回的全路径(路径+文件名)
				// 保存文件
				if (filename == null) {
					MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
					return;
				}
				try {
					tplSet.saveToFile(filename);
				} catch (IOException e1) {
					e1.printStackTrace();
					MessageDialog.openError(s, "错误！", "保存文件失败！请检查文件是否可以写入！");
				}
				MessageDialog.openInformation(s, "提示！", "模板集保存成功！");
//				if (workPath == null) {
					File f = new File(filename);
					workPath = f.getParent();
//				}
				tx_setFileName.setText(filename);
			}
		});
		btn_newTplSet.setText("新建");
		FormData fd_btn_newTplSet = new FormData();
		fd_btn_newTplSet.left = new FormAttachment(lb_tplSet, 6);
		fd_btn_newTplSet.top = new FormAttachment(0, 10);
		btn_newTplSet.setLayoutData(fd_btn_newTplSet);
		btn_newTplSet.setToolTipText("新建模板集");

		// 打开模板集
		Button btn_openTplSet = new Button(this, SWT.NONE);
		btn_openTplSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				tx_setFileName.setText("");
				t_template.removeAll();

				Shell s = Display.getCurrent().getActiveShell();

				FileDialog d = new FileDialog(s, SWT.OPEN);
				if (tx_setFileName.getText() != ""){
					String path = tx_setFileName.getText();
					d.setFilterPath(path);
				}
//				String path = System.getProperty("user.dir");
//				d.setFilterPath(path);
				d.setText("打开已有的模板集文件！");
				d.setFilterNames(new String[] { "模板集文件(*.tplset)" });
				d.setFilterExtensions(new String[] { "*.tplset" });
				String filename = d.open();// 返回的全路径(路径+文件名)
				if (filename == null) {
					MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
					return;
				} else if (filename.equals("")) {
					MessageDialog.openError(s, "错误！", "请选择有效的文件名！");
					return;
				}
				readTpl(filename);
				GVar.config.setProperty("TPLFileName",filename);
			}
		});
		FormData fd_btn_openTplSet = new FormData();
		fd_btn_openTplSet.left = new FormAttachment(btn_newTplSet, 6);
		fd_btn_openTplSet.top = new FormAttachment(0, 10);
		btn_openTplSet.setLayoutData(fd_btn_openTplSet);
		btn_openTplSet.setText("打开");
		btn_openTplSet.setToolTipText("打开已有的模板集");

		// 保存模板集
		Button btn_saveTplSet = new Button(this, SWT.NONE);
		btn_saveTplSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = Display.getCurrent().getActiveShell();
				TplSet tplSet = GVar.gTplSet;
				if (tplSet == null) {
					// MessageDialog.openError(s, "错误", "模板集为空");
					tplSet = new TplSet();
					GVar.gTplSet = tplSet;
				}
				String filename = tplSet.getFullPathFileName();
				if (filename == null) {
					// 选择文件名和路径
					FileDialog d = new FileDialog(s, SWT.SAVE);
					d.setText("保存模板集文件！");
					if (workPath != null)
						d.setFilterPath(workPath);
					d.setFilterNames(new String[] { "模板集文件(*.tplset)" });
					d.setFilterExtensions(new String[] { "*.tplset" });
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
					tplSet.saveToFile(filename);
					MessageDialog.openInformation(s, "提示！", "模板集保存成功！");
				} catch (IOException e1) {
					e1.printStackTrace();
					MessageDialog.openError(s, "错误！", "保存文件失败！请检查文件是否可以写入！");
				}
				if (workPath == null) {
					File f = new File(filename);
					workPath = f.getParent();
				}
				tx_setFileName.setText(filename);
			}
		});
		FormData fd_btn_saveTplSet = new FormData();
		fd_btn_saveTplSet.left = new FormAttachment(btn_openTplSet, 6);
		fd_btn_saveTplSet.top = new FormAttachment(0, 10);
		btn_saveTplSet.setLayoutData(fd_btn_saveTplSet);
		btn_saveTplSet.setText("保存");
		btn_saveTplSet.setToolTipText("保存模板集");

		// Label lb_setFileName = new Label(this, SWT.NONE);
		// lb_setFileName.setFont(SWTResourceManager.getFont("Tahoma", 8,
		// SWT.NORMAL));
		// FormData fd_lb_setFileName = new FormData();
		// fd_lb_setFileName.top = new FormAttachment(btn_openTplSet, 5,
		// SWT.TOP);
		// fd_lb_setFileName.left = new FormAttachment(btn_saveTplSet, 6);
		// fd_lb_setFileName.right = new FormAttachment(100, -19);
		// lb_setFileName.setLayoutData(fd_lb_setFileName);
		// lb_setFileName.setText("模板集配置文件名称");
		tx_setFileName = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		tx_setFileName.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.NORMAL));
		FormData fd_tx_setFileName = new FormData();
		fd_tx_setFileName.bottom = new FormAttachment(btn_saveTplSet, 0, SWT.BOTTOM);
		fd_tx_setFileName.top = new FormAttachment(btn_saveTplSet, 0, SWT.TOP);
		fd_tx_setFileName.left = new FormAttachment(btn_saveTplSet, 6);
		fd_tx_setFileName.right = new FormAttachment(100, -19);
		tx_setFileName.setLayoutData(fd_tx_setFileName);

		Label lb_separator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_lb_separator = new FormData();
		fd_lb_separator.top = new FormAttachment(btn_openTplSet, 5);
		fd_lb_separator.right = new FormAttachment(100, -5);
		fd_lb_separator.left = new FormAttachment(0, 5);
		lb_separator.setLayoutData(fd_lb_separator);

		Button btn_all = new Button(this, SWT.NONE);
		btn_all.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tv_template.getTable().getItemCount() == 0)
					return;
				if (tv_template.getCheckedElements().length == tv_template.getTable().getItemCount())
					tv_template.setAllChecked(false);
				else
					tv_template.setAllChecked(true);
				checkState();
			}
		});
		FormData fd_btn_all = new FormData();
		fd_btn_all.top = new FormAttachment(lb_separator, 5);
		fd_btn_all.left = new FormAttachment(0, 10);
		btn_all.setLayoutData(fd_btn_all);
		btn_all.setText("全选");

		Button btn_inverse = new Button(this, SWT.NONE);
		btn_inverse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object ao[] = tv_template.getCheckedElements();
				tv_template.setAllChecked(true);
				for (Object o : ao) {
					tv_template.setChecked(o, false);
				}
				checkState();
			}
		});
		FormData fd_btn_inverse = new FormData();
		fd_btn_inverse.left = new FormAttachment(btn_all, 5);
		fd_btn_inverse.top = new FormAttachment(lb_separator, 5);
		btn_inverse.setLayoutData(fd_btn_inverse);
		btn_inverse.setText("反选");

		btn_addTpl = new Button(this, SWT.NONE);
		btn_addTpl.setText("新增模板");
		btn_addTpl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// 弹出新增tpl的窗口
				TplDialog d = new TplDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, TplForm.NEW,
						workPath);
				d.open();
				Tpl newtpl = d.getTpl();
				if (newtpl == null)
					return;
				newtpl.setChecked(true);
				if (newtpl != null) {
					if (GVar.gTplSet == null) {
						GVar.gTplSet = new TplSet();
					}
					GVar.gTplSet.addTpl(newtpl);
					tv_template.add(newtpl);

					if (workPath == null) {
						workPath = newtpl.getPath();
					}
				}
				String filename = tx_setFileName.getText();
				try {
					GVar.gTplSet.saveToFile(filename);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (workPath == null) {
					File f = new File(filename);
					workPath = f.getParent();
				}

				checkState();
			}
		});
		FormData fd_btn_addTpl = new FormData();
		fd_btn_addTpl.left = new FormAttachment(btn_inverse, 20);
		fd_btn_addTpl.top = new FormAttachment(lb_separator, 5);
		btn_addTpl.setLayoutData(fd_btn_addTpl);

		Button btn_delTpl = new Button(this, SWT.NONE);
		btn_delTpl.setText("删除选中模板");
		btn_delTpl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = Display.getCurrent().getActiveShell();
				Object ao[] = tv_template.getCheckedElements();
				if (ao.length == 0) {
					MessageDialog.openInformation(s, "提示", "没有选中的条目！");
					return;
				}
				boolean b = MessageDialog.openQuestion(s, "提示", "您选中了" + ao.length + "个模板，确定删除？\n注：从模板集中删除，模板文件不会删除");
				if (!b) {
					return;
				}
				for (Object o : ao) {
					GVar.gTplSet.delTpl(((Tpl) o).getName());
					tv_template.remove(o);
				}
				checkState();
			}
		});
		FormData fd_btn_delTpl = new FormData();
		fd_btn_delTpl.top = new FormAttachment(lb_separator, 5);
		fd_btn_delTpl.left = new FormAttachment(btn_addTpl, 5);
		btn_delTpl.setLayoutData(fd_btn_delTpl);

		tv_template = CheckboxTableViewer.newCheckList(this, SWT.BORDER | SWT.FULL_SELECTION);
		tv_template.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				checkState();
			}
		});
		tv_template.setAllChecked(true);
		t_template = tv_template.getTable();
		t_template.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Shell s = Display.getCurrent().getActiveShell();
				if (t_template.getSelectionIndex() == -1) {
					MessageDialog.openError(s, "错误！", "请用鼠标选中一条记录！");
				}
				TableItem item = t_template.getSelection()[0];
				Tpl t = (Tpl) item.getData();
				TplDialog dlg = new TplDialog(s, SWT.NONE, TplForm.EDIT, workPath, t);
				dlg.open();
				t = dlg.getTpl();
				GVar.gTplSet.addTpl(t);
				item.setData(t);
				item.setText(0, t.getName());
				item.setText(2, t.getType());
				item.setText(3, t.getPath());
				item.setText(1, t.getNameRegular());
				item.setText(4, t.getRemark());
			}
		});
		t_template.setToolTipText("选择需要生成代码的模板，双击编辑模板");
		t_template.setHeaderVisible(true);
		FormData fd_t_template = new FormData();
		fd_t_template.bottom = new FormAttachment(100, -10);
		fd_t_template.right = new FormAttachment(100, -10);
		fd_t_template.top = new FormAttachment(btn_addTpl, 5);
		fd_t_template.left = new FormAttachment(0, 10);
		t_template.setLayoutData(fd_t_template);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tv_template, SWT.NONE);
		TableColumn tc_fileName = tableViewerColumn.getColumn();
		tc_fileName.setWidth(150);
		tc_fileName.setText("模板文件名");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tv_template, SWT.NONE);
		TableColumn tc_outFileNameRule = tableViewerColumn_2.getColumn();
		tc_outFileNameRule.setWidth(200);
		tc_outFileNameRule.setText("生成文件名规则");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tv_template, SWT.NONE);
		TableColumn tc_type = tableViewerColumn_4.getColumn();
		tc_type.setWidth(100);
		tc_type.setText("模板类型");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv_template, SWT.NONE);
		TableColumn tc_filePath = tableViewerColumn_1.getColumn();
		tc_filePath.setWidth(200);
		tc_filePath.setText("模板文件路径");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tv_template, SWT.NONE);
		TableColumn tc_remark = tableViewerColumn_3.getColumn();
		tc_remark.setWidth(200);
		tc_remark.setText("备注");

		tv_template.setContentProvider(new IStructuredContentProvider() {
			@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Collection<?>) {
					return ((Collection<Tpl>) inputElement).toArray();
				} else {
					return new Object[0];
				}
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		});
		tv_template.setLabelProvider(new ITableLabelProvider() {
			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof Tpl) {
					Tpl e = (Tpl) element;
					if (columnIndex == 0) {
						return e.getName();
					} else if (columnIndex == 2) {
						return e.getType();
					} else if (columnIndex == 3) {
						return e.getPath();
					} else if (columnIndex == 1) {
						return e.getNameRegular();
					} else if (columnIndex == 4) {
						return e.getRemark();
					}
				}
				return null;
			}

			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}
		});

		this.readTpl(GVar.config.getProperty("TPLFileName"));

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	protected void checkState() {
		for (Tpl t : GVar.gTplSet.tpls()) {
			t.setChecked(false);
		}
		for (Object o : tv_template.getCheckedElements()) {
			Tpl t = (Tpl) o;
			t.setChecked(true);
		}
		GVar.gWin.freshState();
	}

	public boolean isReady() {
		return tv_template.getCheckedElements().length > 0 ? true : false;
	}

	private void readTpl(String filename) {
		
		if (filename == null || filename == "")
			return;

		tx_setFileName.setText("");
		t_template.removeAll();

		Shell s = Display.getCurrent().getActiveShell();

		TplSet tplSet = new TplSet();
		GVar.gTplSet = tplSet;
		try {
			tplSet.readFile(filename);
		} catch (DocumentException e1) {
			MessageDialog.openError(s, "错误！", "读取文件错误！");
			e1.printStackTrace();
			return;
		}
		tx_setFileName.setText(filename);
		tv_template.setInput(tplSet.tpls());
		tv_template.setAllChecked(true);
		// for (Tpl t:tplSet.tpls()){
		// TableItem item = new TableItem(t_template, SWT.NONE);
		// item.setText(0, t.getName());
		// item.setText(1, t.getPath());
		// item.setText(2, t.getNameRegular());
		// item.setText(3, t.getRemark());
		// item.setChecked(true);
		// }
		for (TableItem ti : t_template.getItems()) {
			Tpl t = (Tpl) ti.getData();
			ti.setChecked(t.isChecked());
		}
		// MessageDialog.openInformation(s, "提示！", "打开模板集文件成功！");
		workPath = tplSet.path();
//		checkState();

	}
}
