package util.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.eclipse.swt.internal.ole.win32.COM;

import com.sybase.stf.powerdesigner.PdCommon.Application;
import com.sybase.stf.powerdesigner.PdPDM.Column;
import com.sybase.stf.powerdesigner.PdPDM.Key;
import com.sybase.stf.powerdesigner.PdPDM.Model;
import com.sybase.stf.powerdesigner.PdPDM.PhysicalDiagram;
import com.sybase.stf.powerdesigner.PdPDM.Reference;
import com.sybase.stf.powerdesigner.PdPDM.ReferenceJoin;
import com.sybase.stf.powerdesigner.PdPDM.Table;
import com.sybase.stf.powerdesigner.PdPDM.TableSymbol;
import com.sybase.stf.powerdesigner.PdPDM.View;

import entity.UColumn;
import entity.UDomain;
import entity.UModel;
import entity.UReference;
import entity.UTable;
import entity.UView;

public class PdmUtil {
	private static Application pdApp;

	private static Model pdModel;

	static public UModel OpenModel(String filename) {
		UModel dm = null;
		try {
			COM.OleInitialize(0);
			pdApp = Application.getInstance();
			pdModel = new Model(pdApp.OpenModel(filename));
			dm = new UModel();
			dm.tableCnt = pdModel.GetTables().GetCount();
			dm.viewCnt = pdModel.GetViews().GetCount();
			File f = new File(filename.trim());
			dm.fileName = f.getName();
			dm.filePath = f.getPath();
			dm.name = pdModel.GetName();
			dm.code = pdModel.GetCode();

			dm.tableMap = new HashMap<String, UTable>();
			for (int i = 0; i < pdModel.GetTables().GetCount(); ++i) {
				Table t = new Table(pdModel.GetTables().Item(i));
				try {
					UTable tp = GetUTable(t);
					dm.tableMap.put(t.GetCode(), tp);
				}
				catch (Exception e){
					
				}
			}

			for (int i = 0; i < pdModel.GetReferences().GetCount(); ++i) {
				Reference r = new Reference(pdModel.GetReferences().Item(i));
				UReference ucr = GetUReference(r);
				dm.addRef(ucr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return dm;
	}

	static public UModel UseOpenedModel() {
		UModel dm = null;
		try {
			COM.OleInitialize(0);
			Application pdApp = Application.getInstance();
			pdModel = new Model(pdApp.GetActiveModel());
			dm = new UModel();
			ArrayList<PhysicalDiagram> alPD = new ArrayList<PhysicalDiagram>();
			ArrayList<UDomain> domains = new ArrayList<UDomain>();
			for (int i=0;i<pdModel.GetPhysicalDiagrams().GetCount();++i){
				PhysicalDiagram pd = new PhysicalDiagram(pdModel.GetPhysicalDiagrams().Item(i));
				alPD.add(pd);
				domains.add(new UDomain(pd.GetCode(),pd.GetName()));
			}
			Collections.sort(domains, (a,b)->a.name.compareTo(b.name));
			Collections.sort(alPD,(a,b)->a.GetName().compareTo(b.GetName()));
			dm.domains = domains;
			dm.tableCnt = pdModel.GetTables().GetCount();
			dm.viewCnt = pdModel.GetViews().GetCount();
			String filename = pdModel.GetFilename();
			File f = new File(filename.trim());
			dm.fileName = f.getName();
			dm.filePath = f.getAbsolutePath();
			dm.name = pdModel.GetName();
			dm.code = pdModel.GetCode();

			dm.tableMap = new HashMap<String, UTable>();
			dm.viewMap = new HashMap<String, UView>();
			for (int i = 0; i < pdModel.GetTables().GetCount(); ++i) {
				Table t = new Table(pdModel.GetTables().Item(i));
				try {
					UTable tp = GetUTable(t);
					for (PhysicalDiagram pd:alPD){
						for (int j=0;j<pd.GetSymbols().GetCount();++j){
							TableSymbol ts = new TableSymbol(pd.GetSymbols().Item(j));
							if (ts.GetCode().split(" : ")[0].equals(tp.getCode())){
								for (int k=0;k<dm.domains.size();++k){
									if (domains.get(k).code.equals(pd.GetCode())){
										domains.get(k).tables.add(tp);
										tp.domain = domains.get(k);
										break;
									}
								}
								break;
							}
						}
						if (tp.domain != null){
							break;
						}
					}
					dm.tableMap.put(t.GetCode(), tp);
				}
				catch (Exception e){
				}
			}

			for (int i = 0; i < pdModel.GetReferences().GetCount(); ++i) {
				Reference r = new Reference(pdModel.GetReferences().Item(i));
				UReference ucr = GetUReference(r);
				dm.addRef(ucr);
			}
			// 获取所有的视图
			for (int i = 0; i < pdModel.GetViews().GetCount(); ++i) {
				View v = new View(pdModel.GetViews().Item(i));
				UView uv = GetUView(v);
				for (PhysicalDiagram pd:alPD){
					for (int j=0;j<pd.GetSymbols().GetCount();++j){
						TableSymbol ts = new TableSymbol(pd.GetSymbols().Item(j));
						if (ts.GetCode().equals(uv.getCode())){
							for (int k=0;k<dm.domains.size();++k){
								if (domains.get(k).code.equals(pd.GetCode())){
									domains.get(k).views.add(uv);
									uv.domain = domains.get(k);
									break;
								}
							}
							break;
						}
					}
					if (uv.domain != null){
						break;
					}
				}
				dm.viewMap.put(v.GetCode(), uv);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dm;
	}

	private static UView GetUView(View v) {
		UView uv = new UView();
		uv.setName(v.GetName());
		uv.setCode(v.GetCode());
		uv.setComment(v.GetComment() == null ? "" : v.GetComment());
		ArrayList<UColumn> cols = new ArrayList<UColumn>();
		uv.setColumns(cols);

		for (int i = 0; i < v.GetColumns().GetCount(); ++i) {
			Column c = new Column(v.GetColumns().Item(i));
			UColumn ucc = new UColumn();
			ucc.setName(c.GetDisplayName());
			ucc.setCode(c.GetCode());
			ucc.setType(c.GetDataType());
			ucc.setComment(c.GetComment() == null ? "" : c.GetComment());
			cols.add(ucc);
		}
		return uv;

	}

	static public UReference GetUReference(Reference r) {
		UReference ucr = new UReference();
		ucr.setCode(r.GetCode());
		ucr.setName(r.GetIntegrity());
//		System.out.println(ucr.getCode());
		HashMap<String, String> hm = new HashMap<String, String>();
//		if (ucr.getCode().equals("Reference_17")){
//			System.out.println(ucr.getCode());
//		}
//TODO 需要判断引用是否合法		
		for (int i = 0; i < r.GetJoins().GetCount(); ++i) {
			ReferenceJoin rj = new ReferenceJoin(r.GetJoins().Item(i));
			String childcol = (new Column(rj.GetChildTableColumn())).GetCode();
			String parentcol = (new Column(rj.GetParentTableColumn())).GetCode();
			ucr.addColPair(childcol, parentcol);
		}
		ucr.setParentTableCode((new Table(r.GetParentTable())).GetCode());
		ucr.setChildTableCode((new Table(r.GetChildTable())).GetCode());
		return ucr;
	}

	static private UTable GetUTable(Table t) {
		UTable dt = new UTable();
		dt.setName(t.GetName());
		dt.setCode(t.GetCode());
		dt.setComment(t.GetComment() == null ? "" : t.GetComment());

		ArrayList<UColumn> pkey = new ArrayList<UColumn>();
		ArrayList<UColumn> cols = new ArrayList<UColumn>();

		dt.setPrimaryKey(pkey);
		dt.setColumns(cols);

		for (int i = 0; i < t.GetColumns().GetCount(); ++i) {
			Column c = new Column(t.GetColumns().Item(i));
			UColumn ucc = new UColumn();
			ucc.setTable(dt);
			ucc.setName(c.GetName());
			ucc.setCode(c.GetCode());
			ucc.setType(c.GetDataType());
			ucc.setComment(c.GetComment() == null ? "" : c.GetComment());
			ucc.setNullStatus(c.GetNullStatus());
			ucc.setIndex(i);
			cols.add(ucc);
		}
		Key pk = new Key(t.GetPrimaryKey());
		if (pk.getAddress() != 0) {
			for (int i = 0; i < pk.GetColumns().GetCount(); ++i) {
				Column c = new Column(t.GetColumns().Item(i));
				UColumn ucc = dt.getColumnByCode(c.GetCode());
				if (ucc != null)
					pkey.add(ucc);
			}
		}

		return dt;
	}

}
