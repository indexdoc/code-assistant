package bizcompos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.Win;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import util.model.XmlUtil;
import util.parse.CAFun;
import entity.UColumn;
import entity.UTable;
import entity.UView;
import global.GVar;

public class SelectTable extends Composite {
	private Table tb_tableList;
	
	private CheckboxTableViewer ctv_tablelist;
	private UTable currentTable;
	private Text tx_tname;
	private Text tx_tcode;
	private Text tx_remark;
	private Table tb_columns;
	private TableViewer tv_columns;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SelectTable(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Button btn_all = new Button(this, SWT.NONE);
		btn_all.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ctv_tablelist.getTable().getItemCount() == 0)
					return;
				if (ctv_tablelist.getCheckedElements().length == ctv_tablelist.getTable().getItemCount())
						ctv_tablelist.setAllChecked(false);
				else 
					ctv_tablelist.setAllChecked(true);

				if (ctv_tablelist.getCheckedElements().length > 0){
					GVar.gWin.freshState();
				}
				
			}
		});
		Button btn_inverse = new Button(this, SWT.NONE);
		btn_inverse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object ao[] = ctv_tablelist.getCheckedElements();
				ctv_tablelist.setAllChecked(true);
				for (Object o:ao){
					ctv_tablelist.setChecked(o, false);
				}
				if (ctv_tablelist.getCheckedElements().length > 0){
					GVar.gWin.freshState();
				}
			}
		});

		btn_all.setText("\u5168\u9009");
		FormData fd_btn_all = new FormData();
		fd_btn_all.left = new FormAttachment(0, 10);
		fd_btn_all.top = new FormAttachment(0, 10);
		btn_all.setLayoutData(fd_btn_all);
		
		btn_inverse.setText("\u53CD\u9009");
		FormData fd_btn_inverse = new FormData();
		fd_btn_inverse.top = new FormAttachment(0, 10);
		fd_btn_inverse.left = new FormAttachment(btn_all,10);
		btn_inverse.setLayoutData(fd_btn_inverse);
		
		ctv_tablelist = CheckboxTableViewer.newCheckList(this, SWT.BORDER | SWT.FULL_SELECTION);
		ctv_tablelist.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
//				if (ctv_tablelist.getCheckedElements().length > 0){
					GVar.gWin.freshState();
//				}
			}
		});
//		ctv_tablelist.addSelectionChangedListener(new ISelectionChangedListener() {
//			public void selectionChanged(SelectionChangedEvent event) {
////				if (ctv_tablelist.getCheckedElements().length > 0)
////					GVar.gWin.setState(Win.TABLE_SELECTED);
////				else
////					GVar.gWin.setState(Win.MODEL_OPENED);
//			}
//		});
		tb_tableList = ctv_tablelist.getTable();
		tb_tableList.setLinesVisible(true);
		tb_tableList.setHeaderVisible(true);
		tb_tableList.setToolTipText("\u9009\u62E9\u9700\u8981\u751F\u6210\u4EE3\u7801\u7684\u8868\u6A21\u578B");
		FormData fd_tb_tableList = new FormData();
		fd_tb_tableList.top = new FormAttachment(btn_inverse);
		fd_tb_tableList.bottom = new FormAttachment(100, -10);
		fd_tb_tableList.right = new FormAttachment(35, 10);
		fd_tb_tableList.left = new FormAttachment(0, 10);
		tb_tableList.setLayoutData(fd_tb_tableList);
		
		Group gb_tableDetail = new Group(this, SWT.NONE);
		FormData fd_gb_tableDetail = new FormData();
		fd_gb_tableDetail.top = new FormAttachment(tb_tableList, 0, SWT.TOP);
		fd_gb_tableDetail.left = new FormAttachment(tb_tableList);
		
		TableViewerColumn tvc_tcode = new TableViewerColumn(ctv_tablelist, SWT.NONE);
		TableColumn tc_tcode = tvc_tcode.getColumn();
		tc_tcode.setWidth(120);
		tc_tcode.setText("\u8868\u7F16\u7801");
		
		TableViewerColumn tvc_tname = new TableViewerColumn(ctv_tablelist, SWT.NONE);
		TableColumn tc_tname = tvc_tname.getColumn();
		tc_tname.setWidth(150);
		tc_tname.setText("\u8868\u540D");
		
		TableViewerColumn tvc_tremark = new TableViewerColumn(ctv_tablelist, SWT.NONE);
		TableColumn tc_tremark = tvc_tremark.getColumn();
		tc_tremark.setWidth(200);
		tc_tremark.setText("\u5907\u6CE8");
		gb_tableDetail.setLayout(new GridLayout(4, false));
		fd_gb_tableDetail.bottom = new FormAttachment(100, -5);
		fd_gb_tableDetail.right = new FormAttachment(100, -10);
		gb_tableDetail.setLayoutData(fd_gb_tableDetail);
		
		Label label = new Label(gb_tableDetail, SWT.NONE);
		label.setText("\u8868\u540D\uFF1A");
		
		tx_tname = new Text(gb_tableDetail, SWT.BORDER);
		tx_tname.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));
		GridData gd_tx_tname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tx_tname.widthHint = 140;
		tx_tname.setLayoutData(gd_tx_tname);
		tx_tname.setEditable(false);
		
		Label label1 = new Label(gb_tableDetail, SWT.NONE);
		label1.setText("\u7F16\u7801\uFF1A");
		
		tx_tcode = new Text(gb_tableDetail, SWT.BORDER);
		tx_tcode.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));
		tx_tcode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		tx_tcode.setEditable(false);
		
		Label label2 = new Label(gb_tableDetail, SWT.NONE);
		label2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label2.setText("\u5907\u6CE8\uFF1A");
		
		tx_remark = new Text(gb_tableDetail, SWT.BORDER);
		tx_remark.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.NORMAL));
		tx_remark.setEditable(false);
		GridData gd_tx_remark = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_tx_remark.widthHint = 194;
		tx_remark.setLayoutData(gd_tx_remark);
		
		tv_columns = new TableViewer(gb_tableDetail, SWT.BORDER | SWT.FULL_SELECTION);
		tb_columns = tv_columns.getTable();
		tb_columns.setHeaderVisible(true);
		GridData gd_tb_columns = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 3);
		gd_tb_columns.heightHint = 283;
		tb_columns.setLayoutData(gd_tb_columns);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv_columns, SWT.NONE);
		TableColumn tc_ccode = tableViewerColumn_1.getColumn();
		tc_ccode.setWidth(100);
		tc_ccode.setText("\u7F16\u7801");
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tv_columns, SWT.NONE);
		TableColumn tc_cname = tableViewerColumn.getColumn();
		tc_cname.setWidth(80);
		tc_cname.setText("\u5217\u540D");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tv_columns, SWT.NONE);
		TableColumn tc_ctype = tableViewerColumn_2.getColumn();
		tc_ctype.setWidth(100);
		tc_ctype.setText("\u6570\u636E\u7C7B\u578B");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tv_columns, SWT.NONE);
		TableColumn tc_climits = tableViewerColumn_4.getColumn();
		tc_climits.setWidth(100);
		tc_climits.setText("数据范围");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tv_columns, SWT.NONE);
		TableColumn tc_cremark = tableViewerColumn_3.getColumn();
		tc_cremark.setWidth(150);
		tc_cremark.setText("\u8BF4\u660E");
		
		Button btn_toXml = new Button(this, SWT.NONE);
		btn_toXml.setText("导出模型为XML文件");
		btn_toXml.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = getShell();
				String filename;
				// 选择文件名和路径
				FileDialog d = new FileDialog(s, SWT.SAVE);
				d.setText("导出数据模型为XML文件！");
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
					XmlUtil.saveAsXML(GVar.gModel, filename);
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageDialog.openError(s, "错误！", "保存文件失败！请检查文件是否可以写入！");
				}
				MessageDialog.openInformation(s, "提示！", "数据模型导出成功！");
			}
		});
		FormData fd_btn_toXml = new FormData();
		fd_btn_toXml.top = new FormAttachment(btn_all, 0, SWT.TOP);
		fd_btn_toXml.right = new FormAttachment(gb_tableDetail, 0, SWT.LEFT);
		btn_toXml.setLayoutData(fd_btn_toXml);
		
		ctv_tablelist.setContentProvider(new IStructuredContentProvider(){
		       public Object[] getElements(Object inputElement) {
		            if(inputElement instanceof List){
		                return ((List)inputElement).toArray();
		            }else{
		                return new Object[0];
		            }
		        }
		        public void dispose() {
		        }
		        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		        }
		});
		ctv_tablelist.setLabelProvider(new ITableLabelProvider(){
		       public String getColumnText(Object element, int columnIndex) {
		            if (element instanceof UTable){
		            	UTable e = (UTable)element;
		                if(columnIndex == 0){
		                    return e.getCode();
		                }else if(columnIndex == 1){
		                    return e.getName();
		                }else if(columnIndex == 2){
		                	return e.getComment();
		                }
		            }
		            if (element instanceof UView){
		            	UView e = (UView)element;
		                if(columnIndex == 0){
		                    return e.getCode();
		                }else if(columnIndex == 1){
		                    return e.getName();
		                }else if(columnIndex == 2){
		                	return e.getComment();
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
		
		tv_columns.setContentProvider(new IStructuredContentProvider(){
		       public Object[] getElements(Object inputElement) {
		            if(inputElement instanceof List){
		                return ((List)inputElement).toArray();
		            }else{
		                return new Object[0];
		            }
		        }
		        public void dispose() {
		        }
		        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		        }
		});
		
		tv_columns.setLabelProvider(new ITableLabelProvider(){
		       public String getColumnText(Object element, int columnIndex) {
		            if (element instanceof UColumn){
		            	UColumn e = (UColumn)element;
		                if(columnIndex == 0){
		                    return e.getCode();
		                }else if(columnIndex == 1){
		                    return e.getName();
		                }else if(columnIndex == 2){
		                	return e.getType();
		                }else if(columnIndex == 3){
		                	return e.getLimits();
		                }else if(columnIndex == 4){
		                	if (currentTable == null)
		                		return null;
		                	return CAFun.GetColRemark(global.GVar.gModel,currentTable,e);
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
		tb_tableList.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = tb_tableList.getSelection();
				if (items == null)
					return;
				if (items.length == 0){
					return;
				}
				currentTable = GVar.gModel.getTable(items[0].getText(0));
				if (currentTable == null)
					return;
				UTable t = currentTable;
				tx_tname.setText(t.getName());
				tx_tcode.setText(t.getCode());
				tx_remark.setText( t.getComment() == null?"":t.getComment());
				tv_columns.setInput(t.getColumns());
			}
		});
	}

	public void refresh(){
		if (global.GVar.gModel != null){
			Collection<Object> al = new ArrayList<Object>();
			al.addAll(global.GVar.gModel.getTableList());
			ctv_tablelist.setInput(al);
		}
	}
	
	public ArrayList<UTable> getSelectedTables(){
		ArrayList<UTable> al = new ArrayList<UTable>();
		for (Object o:ctv_tablelist.getCheckedElements()){
			UTable t = (UTable)o;
			al.add(t);
		}
		if (global.GVar.gModel != null)
			global.GVar.gModel.selectedTables = al;	
		return al;
	}
	public boolean isReady(){
		if (GVar.gModel == null)
			return false;
		this.getSelectedTables();
		if (GVar.gModel.selectedTables == null || GVar.gModel.selectedTables.size()==0)
			return false;
		if (ctv_tablelist.getCheckedElements().length == 0)
			return false;
		return true;
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
