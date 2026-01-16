package util.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.UColumn;
import entity.UModel;
import entity.UReference;
import entity.UTable;
import global.GVar;

public class DbmsUtil {
	
	static public String URL_ORACLE = "jdbc:oracle:thin:";
	static public String URL_SQLSERVER = "jdbc:microsoft:sqlserver:";
	static public String URL_MYSQL = "jdbc:mysql:";
	static public String URL_POSTGRESQL = "jdbc:postgresql:";

	static public String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	static public String DRIVER_SQLSERVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	static public String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	static public String DRIVER_POSTGRESQL = "org.postgresql.Driver";

	static private String driverType = null;
	static private DatabaseMetaData  dmd = null;
	static private String modelName = null;
	
	static public ArrayList<String> schemas = null;
	
	static public ArrayList<String> GetSchemas(String url,String user,String password){
		driverType = null;
		schemas = new ArrayList<String>();
		try {
			if (URL_ORACLE.equals(url.substring(0,URL_ORACLE.length()))){
				driverType = DRIVER_ORACLE;
			}else if (URL_SQLSERVER.equals(url.substring(0,URL_SQLSERVER.length()))){
				driverType = DRIVER_SQLSERVER;
			}else if (URL_MYSQL.equals(url.substring(0,URL_MYSQL.length()))){
				driverType = DRIVER_MYSQL;
			}else if (URL_POSTGRESQL.equals(url.substring(0,URL_POSTGRESQL.length()))){
				driverType = DRIVER_POSTGRESQL;
			}else{
				return null;
			}
			Class.forName(driverType).newInstance();
		    Connection conn= DriverManager.getConnection(url,user,password);
		    if (conn == null)
		    	return null;
		    dmd = conn.getMetaData();
            ResultSet rs = dmd.getSchemas();   
            while (rs.next()){   
                String s = rs.getString("TABLE_SCHEM");
                schemas.add(s);
            }
            return schemas;

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	static public UModel OpenDBMS(String url,String user,String password, String schema) {
		UModel model = new UModel();
		modelName = schema;
		model.url = url;
		try{
		    model.name = schema;
		    model.tableMap = new HashMap<String, UTable>();
            ResultSet rs = dmd.getTables(null, schema, "%", new String[]{"TABLE"});   
            while (rs.next())   
            {   
            	UTable t = new UTable();
            	t.setCode(rs.getString("TABLE_NAME"));
            	t.setName(rs.getString("TABLE_NAME"));
            	t.setComment(rs.getString("REMARKS"));
            	t.setColumns(getColumns(t.getCode()));
            	t.setPrimaryKey(getPrimaryKey(user,t));
            	model.tableMap.put(t.getCode(), t);
            } 
            doGetRefs(model);
        } catch (Exception e){
			e.printStackTrace();
			return null;
		}
		model.tableCnt=model.tableMap.values().size();
		GVar.gModel = model;
		return model;
	}

	private static void doGetRefs(UModel model) throws SQLException {
		ResultSet rs = dmd.getImportedKeys(null, model.getName(), null);
		while (rs.next()) {
			// primary key table name
			String parentTableName = rs.getString("PKTABLE_NAME");
			// primary key column name
			String parentColumnName = rs.getString("PKCOLUMN_NAME");
			// foreign key table name being exported
			String childTableName = rs.getString("FKTABLE_NAME");
			// foreign key column name being exported
			String childColumnName = rs.getString("FKCOLUMN_NAME");
            // foreign key name (may be null)   
            String fkName = rs.getString("FK_NAME");  
			
			UReference ucr = new UReference();
			
			ucr.setCode(fkName);
			ucr.setName(fkName);
			ucr.addColPair(childColumnName, parentColumnName); //jdbc每个foreign key只保留一对列。
			ucr.setParentTableCode(parentTableName);
			ucr.setChildTableCode(childTableName);
			
			model.addRef(ucr);
		}
	}
	private static ArrayList<UColumn> getColumns(String tablecode) throws SQLException {
        ResultSet rs = dmd.getColumns(null, modelName, tablecode, "%");
        ArrayList<UColumn> cols = new ArrayList<UColumn>();
        while (rs.next())   
        {   
        	UColumn c = new UColumn();
            c.setCode(rs.getString("COLUMN_NAME"));   
            c.setName(rs.getString("COLUMN_NAME"));   
            c.setType(rs.getString("TYPE_NAME"));
            c.setLimits(rs.getString("COLUMN_SIZE"));
            cols.add(c);
        }
		return cols;
	}
	private static ArrayList<UColumn> getPrimaryKey(String schema, UTable table) throws SQLException {
		ResultSet rs = dmd.getPrimaryKeys(null, schema, table.getCode());
		ArrayList<UColumn> pkey = new ArrayList<UColumn>();
        while (rs.next())   
        {   
            String columnName = rs.getString("COLUMN_NAME");   
            UColumn c = table.getColumnByCode(columnName);
            if (c != null)
            	pkey.add(c);
        }

		return pkey;
	}
	@SuppressWarnings("unused")
	private static String getModelName(String url, String driverType) {
		String name = null;
		if (driverType == DRIVER_ORACLE){
			String str[] = url.split(":");
			name = str[str.length-1];
		}else if (driverType == DRIVER_SQLSERVER){
			String str[] = url.split("=");
			name = str[str.length-1];
		}else if (driverType == DRIVER_MYSQL){
			String str[] = url.split("/");
			name = str[str.length-1];
		}else if (driverType == DRIVER_POSTGRESQL){
			String str[] = url.split("/");
			name = str[str.length-1];
		}else{
			return null;
		}
		return name;
	}
}
