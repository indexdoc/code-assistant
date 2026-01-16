package bizcompos;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.editors.KTableCellEditorComboText;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;
import entity.TypeMap;

class TypeMapKTableDetail extends KTableDefaultModel {
	private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(
			FixedCellRenderer.STYLE_FLAT
					| TextCellRenderer.INDICATION_FOCUS_ROW);
	private final TextCellRenderer m_textRenderer = new TextCellRenderer(
			TextCellRenderer.INDICATION_FOCUS_ROW);

	private TypeMap typeMap;
	private ArrayList<String[]> tableContent ;
//	private HashMap<Integer,String[]> idxTableContent;
//	private HashMap<Integer,String[]> filteredContent;

	public void setContent(TypeMap typeMap) {
		this.typeMap = typeMap;
		tableContent = typeMap.toTableWithHeader();
//		idxTableContent = new HashMap<Integer,String[]>();
//		for (int i=0;i<tableContent.size();++i){
//			idxTableContent.put(new Integer(i), tableContent.get(i));
//		}
//		filteredContent = (HashMap<Integer,String[]>)idxTableContent.clone();
	}

	public TypeMapKTableDetail(TypeMap typeMap) {
		this.setContent(typeMap);
		initialize();
	}

	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		if (isFixedCell(col, row)) {
			return null;
		}
		switch (col) {
		case 0: {
			KTableCellEditorComboText e = new KTableCellEditorComboText();
			e.setItems(typeMap.getDbs());
			return e;
		}
		case 1: {
			return new KTableCellEditorText();
		}
		case 2: {
			KTableCellEditorComboText e = new KTableCellEditorComboText();
			e.setItems(typeMap.getLangs());
			return e;
		}
		case 3: {
			return new KTableCellEditorText();
		}
		case 4: {
			return new KTableCellEditorText();
		}
		default:
			return null;
		}
	}

	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (isFixedCell(col, row))
			return m_fixedRenderer;
		return m_textRenderer;
	}

	@Override
	public int doGetColumnCount() {
		return TypeMap.HEADER_NAME.length;
	}

	@Override
	public Object doGetContentAt(int col, int row) {
		return tableContent.get(row)[col];
	}

	@Override
	public int doGetRowCount() {
		return tableContent.size();
	}

	@Override
	// 编辑表格后执行
	public void doSetContentAt(int col, int row, Object value) {
		//先修改content的内容
		String oldcellvalue = tableContent.get(row)[col] ;
		String newcellvalue = (String) value;
		if(oldcellvalue.equals(newcellvalue))
			return;
//		String[] v = tableContent.get(row);
		tableContent.get(row)[col] = newcellvalue;
		
		//如果修改了KEY的内容
		if (col ==0 || col==1 ||col ==2){
			String[] v= tableContent.get(row);
			Integer idx = typeMap.get(v[0],v[1],v[2]);
			if (idx == null){//新的主键 
				String[] oldkey = new String[3];
				oldkey[0] = v[0];
				oldkey[1] = v[1];
				oldkey[2] = v[2];
				oldkey[col] = oldcellvalue;
				typeMap.remove(oldkey[0],oldkey[1],oldkey[2]);
				typeMap.add(v[0],v[1],v[2],v[3],v[4]);				
			}else if (idx != row -1){//如果与已有记录重复
				//提示与已有记录重复，取消操作
				MessageDialog.openError(Display.getCurrent().getActiveShell() , "错误，操作取消！", "与第"+(idx)+"行记录冲突！\n请直接修改改行记录！");
				tableContent.get(row)[col] = oldcellvalue;
				this.setContentAt(col, row, oldcellvalue);
			}else{
//				typeMap.refreshMap();
			}
		}
	}

	@Override
	public int getInitialColumnWidth(int column) {
		return 90;
	}

	@Override
	public int getInitialRowHeight(int row) {
		return 20;
	}

	@Override
	public int getFixedHeaderColumnCount() {
		return 0;
	}

	@Override
	public int getFixedHeaderRowCount() {
		return 1;
	}

	@Override
	public int getFixedSelectableColumnCount() {
		return 0;
	}

	@Override
	public int getFixedSelectableRowCount() {
		return 0;
	}

	@Override
	public int getRowHeightMinimum() {
		return 18;
	}

	@Override
	public boolean isColumnResizable(int col) {
		return true;
	}

	@Override
	public boolean isRowResizable(int row) {
		return false;
	}

	public void addNullRow() {
		tableContent.add(new String[]{"","","","","",""});
	}

	public void filter(String db, String lang) {
		tableContent = typeMap.toTableWithHeader();
		if (db.equals("ALL")){
			db="";
		}
		if (lang.equals("ALL")){
			lang="";
		}
		if (db.equals("") && lang.equals("")){
			return;
		}
		ArrayList<String[]> newtc = new ArrayList<String[]>();
		newtc.add(tableContent.get(0));
//		for (int i=1;i<tableContent.size();++i){
		for (String[] row:tableContent){
			if ((row[0].equals(db)||db.equals("")) 
					&& (row[2].equals(lang)||lang.equals("")))
			{
				newtc.add(row);
			}
		}
		this.tableContent = newtc;
	}

	public void delRow(int rowid) {
		String[] v = this.tableContent.get(rowid);
		typeMap.remove(v[0], v[1], v[2]);
		this.tableContent.remove(rowid);
	}
}
