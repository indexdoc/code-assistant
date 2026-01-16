package main;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import bizcompos.EditTplSet;
import bizcompos.EditTypeMap;
import bizcompos.OpenModel;
import bizcompos.SelectTable;
import bizcompos.SelectView;
import bizcompos.ToCode;
import entity.TypeMap;
import entity.UTable;
import entity.UView;


public class Win extends Composite {
	
	private CTabFolder tabFolder;
	private EditTypeMap editTypeMap;
	private OpenModel openModel ;
	private SelectTable selectTable;
	private SelectView selectView;

	private EditTplSet editTplSet;
	private ToCode toCode;
	
	private CTabItem tbtm_editTypeMap;
	private CTabItem tbtm_openModel   ;
	private CTabItem tbtm_selectTable ;
	private CTabItem tbtm_selectView ;
	
	private CTabItem tbtm_editTemplate;
	private CTabItem tbtm_toCode    ;
	
	public static String INI = "INI";
	public static String MODEL_OPENED = "MODEL_OPENED";
	public static String TABLE_SELECTED = "TABLE_SELECTED";
	public static String VIEW_SELECTED = "VIEW_SELECTED";
	public static String TPL_SELECTED = "TPL_SELECTED";
//	public static String TYPE_MAP_SELECTED = "TYPE_MAP_SELECTED";

	private String state = INI;
	
	public void freshState(){
		//0、刚打开的时候 表模型、模板、生成都失效；
		//1、判断各个tab当前的状态
		if (openModel.isReady()){
			setEnable(tbtm_selectTable,true);
			setEnable(tbtm_selectView,true);
		}else{
			setEnable(tbtm_selectTable,false);
			setEnable(tbtm_selectView,false);
		}
		
		if (editTypeMap.isReady() && (selectTable.isReady()||selectView.isReady()) && editTplSet.isReady()){
			setEnable(tbtm_toCode,true);
		}else{
			setEnable(tbtm_toCode,false);
		}
//		this.redraw();
	}
	private void setEnable(CTabItem ti,boolean b){
		if (b){
			ti.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD));
		}else{
			ti.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD | SWT.ITALIC));
		}
	}

	private void setState(String s){
		
		if (state == s){
			return;
		}else{
			state = s;
		}
		
		int idx = tabFolder.getSelectionIndex();

		if (s == INI){
			if (tbtm_selectTable != null) {tbtm_selectTable.dispose();tbtm_selectTable =null;}
			if (tbtm_selectView != null) {tbtm_selectView.dispose();tbtm_selectView =null;}
			if (tbtm_editTemplate!= null) {tbtm_editTemplate.dispose();tbtm_editTemplate=null;}
//			if (tbtm_editTypeMap != null) {tbtm_editTypeMap.dispose();tbtm_editTypeMap =null;} 
			if (tbtm_toCode    != null) {tbtm_toCode.dispose();tbtm_toCode =null;} 
			tabFolder.setSelection(0);
		}else if (s == MODEL_OPENED){

//			if (tbtmSelectTable != null) tbtmSelectTable.dispose() ;
			if (tbtm_editTemplate!= null) {tbtm_editTemplate.dispose();tbtm_editTemplate=null;}
//			if (tbtm_editTypeMap != null) {tbtm_editTypeMap.dispose();tbtm_editTypeMap =null;} 
			if (tbtm_toCode    != null) {tbtm_toCode.dispose();tbtm_toCode =null;} 
			 
			if (tbtm_selectTable == null) {
				tbtm_selectTable = new CTabItem(tabFolder, 0);
				tbtm_selectTable.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD));
				tbtm_selectTable.setText("\u9009\u62E9\u8868\u6A21\u578B");
				tbtm_selectTable.setControl(selectTable);
			}
			if (tbtm_selectView == null) {
				tbtm_selectView = new CTabItem(tabFolder, 0);
				tbtm_selectView.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD));
				tbtm_selectView.setText("\u9009\u62E9\u8868\u6A21\u578B");
				tbtm_selectView.setControl(selectView);
			}
		}else if (s == TABLE_SELECTED || s== VIEW_SELECTED){
//			if (tbtm_editTypeMap != null) {tbtm_editTypeMap.dispose();tbtm_editTypeMap =null;} 
			if (tbtm_toCode!= null) {tbtm_toCode.dispose();tbtm_toCode=null;}
			
			if (tbtm_editTemplate == null) {
				tbtm_editTemplate = new CTabItem(tabFolder, 0);
				tbtm_editTemplate.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD));
				tbtm_editTemplate.setText("\u7F16\u8F91\u6A21\u677F");
				tbtm_editTemplate.setControl(editTplSet);
			}
		}
		else if (s == TPL_SELECTED) {
				if (tbtm_toCode == null) {
					tbtm_toCode = new CTabItem(tabFolder, SWT.NONE);
					tbtm_toCode.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD));					
					tbtm_toCode.setText("代码生成");
					tbtm_toCode.setControl(toCode);
					toCode.refresh();
			}
		}
		
		int cnt = tabFolder.getItemCount();
		tabFolder.setSelection(idx>cnt?cnt:idx);
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Win(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		this.setSize(650, 400);

		tabFolder = new CTabFolder(this, SWT.NONE);
		tabFolder.setBorderVisible(true);
		tabFolder.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD));
		tabFolder.setSimple(false);
		openModel = new OpenModel(tabFolder, SWT.NONE);
		selectTable = new SelectTable(tabFolder, SWT.NONE);
		selectView = new SelectView(tabFolder, SWT.NONE);
		editTplSet = new EditTplSet(tabFolder, SWT.NONE);
		editTypeMap = new EditTypeMap(tabFolder, SWT.NONE);
		toCode = new ToCode(tabFolder, SWT.NONE);

		openModel.setLayout(new FormLayout());
		selectTable.setLayout(new FormLayout());
		selectView.setLayout(new FormLayout());
		editTplSet.setLayout(new FormLayout());
		
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection() == tbtm_selectTable||tabFolder.getSelection() == tbtm_selectView){
					if (!openModel.isReady()){
						MessageDialog.openInformation(e.display.getActiveShell(), "错误！", "请先打开数据模型！");
						tabFolder.setSelection(tbtm_openModel);
					}else{
						selectTable.refresh();
						selectView.refresh();
					}
				}else if (tabFolder.getSelection() == tbtm_toCode){
					if (!editTypeMap.isReady()){
						MessageDialog.openInformation(e.display.getActiveShell(), "错误！", "请先编辑数据类型映射关系！");
						tabFolder.setSelection(tbtm_editTypeMap);
					}else if (!editTplSet.isReady()){
						MessageDialog.openInformation(e.display.getActiveShell(), "错误！", "请先编辑模板！");
						tabFolder.setSelection(tbtm_editTemplate);						
					}else if (!selectTable.isReady() && !selectView.isReady()){
						if (!openModel.isReady()){
							MessageDialog.openInformation(e.display.getActiveShell(), "错误！", "请先打开数据模型！");
							tabFolder.setSelection(tbtm_openModel);
						}else{				
							MessageDialog.openInformation(e.display.getActiveShell(), "错误！", "至少需选择一个“表”或者“视图”！");
							tabFolder.setSelection(tbtm_selectTable);
						}
					}else{
						toCode.refresh();
					}
				}
				
			}
		});
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.top = new FormAttachment(0);
		fd_tabFolder.left = new FormAttachment(0);
		fd_tabFolder.bottom = new FormAttachment(100);
		fd_tabFolder.right = new FormAttachment(100);
		tabFolder.setLayoutData(fd_tabFolder);

		tbtm_editTypeMap = new CTabItem(tabFolder, SWT.NONE);
		tbtm_editTypeMap.setText("数据类型映射");
		tbtm_editTypeMap.setControl(editTypeMap);
		
		tbtm_editTemplate = new CTabItem(tabFolder, 0);
		tbtm_editTemplate.setText("编辑模板");
//		tbtm_editTemplate.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD | SWT.ITALIC));
		tbtm_editTemplate.setControl(editTplSet);
		
		tbtm_openModel = new CTabItem(tabFolder, SWT.NONE);
//		tbtm_selectTable.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD | SWT.ITALIC));
		tbtm_openModel.setText("打开数据模型");
		tbtm_openModel.setControl(openModel);

		tbtm_selectTable = new CTabItem(tabFolder, 0);
		tbtm_selectTable.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD | SWT.ITALIC));
		tbtm_selectTable.setText("选择表模型");
		tbtm_selectTable.setControl(selectTable);

		tbtm_selectView = new CTabItem(tabFolder, 0);
		tbtm_selectView.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD | SWT.ITALIC));
		tbtm_selectView.setText("选择视图模型");
		tbtm_selectView.setControl(selectView);

		tbtm_toCode = new CTabItem(tabFolder, SWT.NONE);
		tbtm_toCode.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD | SWT.ITALIC));
		tbtm_toCode.setText("代码生成");
		tbtm_toCode.setControl(toCode);
		
		tabFolder.setSelection(tbtm_editTemplate);
	}
	
	public TypeMap getTypeMap(){
		return this.editTypeMap.typeMap;
	}
	
	public ArrayList<UTable> getSelectedTables(){
		return selectTable.getSelectedTables();
	}
	public ArrayList<UView> getSelectedViews(){
		return selectView.getSelectedViews();
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
