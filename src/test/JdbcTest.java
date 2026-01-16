package test;

import java.sql.Connection;   
import java.sql.DatabaseMetaData;   
import java.sql.DriverManager;   
import java.sql.ResultSet;   
import java.sql.SQLException;   
  
/**  
 * @author daoger  
 *   
 * 2009-9-24  
 */  
public class JdbcTest   
{   
    private DatabaseMetaData dbMetaData = null;   
  
    private Connection con = null;   
  
    public static void main(String[] args){
    	String schemaName = "ONEPAYTEST";
    	JdbcTest jt = new JdbcTest();
    	jt.getDatabaseMetaData();
//    	jt.getDataBaseInformations();
//    	jt.getAllSchemas();
//    	jt.getAllTableList(schemaName);
    	String tableName = "BIZ_PARTNER";
    	jt.getTableColumns(schemaName, tableName);
//    	jt.getAllPrimaryKeys(schemaName, tableName);
//    	jt.getAllImportedKeys(schemaName, tableName);
    }
    private void getDatabaseMetaData()   
    {   
        try  
        {   
            if (dbMetaData == null)   
            {   
                Class.forName("oracle.jdbc.driver.OracleDriver");   
                String url = "jdbc:oracle:thin:@220.165.3.178:14523:xe";   
                String user = "ONEPAYTEST";   
                String password = "ONEPAYTEST369";   
                con = DriverManager.getConnection(url, user, password);   
                dbMetaData = con.getMetaData();   
            }   
        } catch (ClassNotFoundException e)   
        {   
            // TODO: handle ClassNotFoundException   
            e.printStackTrace();   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    public void colseCon()   
    {   
        try  
        {   
            if (con != null)   
            {   
                con.close();   
            }   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 获得数据库的一些相关信息  
     */  
    public void getDataBaseInformations()   
    {   
        try  
        {   
            System.out.println("URL:" + dbMetaData.getURL() + ";");   
            System.out.println("UserName:" + dbMetaData.getUserName() + ";");   
            System.out.println("isReadOnly:" + dbMetaData.isReadOnly() + ";");   
            System.out.println("DatabaseProductName:" + dbMetaData.getDatabaseProductName() + ";");   
            System.out.println("DatabaseProductVersion:" + dbMetaData.getDatabaseProductVersion() + ";");   
            System.out.println("DriverName:" + dbMetaData.getDriverName() + ";");   
            System.out.println("DriverVersion:" + dbMetaData.getDriverVersion());   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 获得该用户下面的所有表  
     */  
    public void getAllTableList(String schemaName)   
    {   
        try  
        {   
            // table type. Typical types are "TABLE", "VIEW", "SYSTEM   
            // TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",   
            // "SYNONYM".   
            String[] types =   
            { "TABLE" };   
            ResultSet rs = dbMetaData.getTables(null, schemaName, "%", types);   
            while (rs.next())   
            {   
                String tableName = rs.getString("TABLE_NAME");   
                // table type. Typical types are "TABLE", "VIEW", "SYSTEM   
                // TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",   
                // "SYNONYM".   
                String tableType = rs.getString("TABLE_TYPE");   
                // explanatory comment on the table   
                String remarks = rs.getString("REMARKS");   
                System.out.println(tableName + "-" + tableType + "-" + remarks);   
            }   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 获得该用户下面的所有视图  
     */  
    public void getAllViewList(String schemaName)   
    {   
        try  
        {   
            String[] types =   
            { "VIEW" };   
            ResultSet rs = dbMetaData.getTables(null, schemaName, "%", types);   
            while (rs.next())   
            {   
                String viewName = rs.getString("TABLE_NAME");   
                // table type. Typical types are "TABLE", "VIEW", "SYSTEM   
                // TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",   
                // "SYNONYM".   
                String viewType = rs.getString("TABLE_TYPE");   
                // explanatory comment on the table   
                String remarks = rs.getString("REMARKS");   
                System.out.println(viewName + "-" + viewType + "-" + remarks);   
            }   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 获得数据库中所有方案名称  
     */  
    public void getAllSchemas()   
    {   
        try  
        {   
            ResultSet rs = dbMetaData.getSchemas();   
            while (rs.next())   
            {   
                String tableSchem = rs.getString("TABLE_SCHEM");   
                System.out.println(tableSchem);   
            }   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 获得表或视图中的所有列信息  
     */  
    public void getTableColumns(String schemaName, String tableName)   
    {   
        try  
        {   
            ResultSet rs = dbMetaData.getColumns(null, schemaName, tableName, "%");   
            while (rs.next())   
            {   
                // table catalog (may be null)   
                String tableCat = rs.getString("TABLE_CAT");   
                // table schema (may be null)   
                String tableSchemaName = rs.getString("TABLE_SCHEM");   
                // table name   
                String tableName_ = rs.getString("TABLE_NAME");   
                // column name   
                String columnName = rs.getString("COLUMN_NAME");   
                // SQL type from java.sql.Types   
                int dataType = rs.getInt("DATA_TYPE");   
                // Data source dependent type name, for a UDT the type name is   
                // fully qualified   
                String dataTypeName = rs.getString("TYPE_NAME");   
                // table schema (may be null)   
                int columnSize = rs.getInt("COLUMN_SIZE");   
                // the number of fractional digits. Null is returned for data   
                // types where DECIMAL_DIGITS is not applicable.   
                int decimalDigits = rs.getInt("DECIMAL_DIGITS");   
                // Radix (typically either 10 or 2)   
                int numPrecRadix = rs.getInt("NUM_PREC_RADIX");   
                // is NULL allowed.   
                int nullAble = rs.getInt("NULLABLE");   
                // comment describing column (may be null)   
                String remarks = rs.getString("REMARKS");   
                // default value for the column, which should be interpreted as   
                // a string when the value is enclosed in single quotes (may be   
                // null)   
                String columnDef = rs.getString("COLUMN_DEF");   
                //                 
                int sqlDataType = rs.getInt("SQL_DATA_TYPE");   
                //                 
                int sqlDatetimeSub = rs.getInt("SQL_DATETIME_SUB");   
                // for char types the maximum number of bytes in the column   
                int charOctetLength = rs.getInt("CHAR_OCTET_LENGTH");   
                // index of column in table (starting at 1)   
                int ordinalPosition = rs.getInt("ORDINAL_POSITION");   
                // ISO rules are used to determine the nullability for a column.   
                // YES --- if the parameter can include NULLs;   
                // NO --- if the parameter cannot include NULLs   
                // empty string --- if the nullability for the parameter is   
                // unknown   
                String isNullAble = rs.getString("IS_NULLABLE");   
                // Indicates whether this column is auto incremented   
                // YES --- if the column is auto incremented   
                // NO --- if the column is not auto incremented   
                // empty string --- if it cannot be determined whether the   
                // column is auto incremented parameter is unknown   
                String isAutoincrement = rs.getString("IS_AUTOINCREMENT");   
                System.out.println(tableCat + "-" + tableSchemaName + "-" + tableName_ + "-" + columnName + "-"  
                        + dataType + "-" + dataTypeName + "-" + columnSize + "-" + decimalDigits + "-" + numPrecRadix   
                        + "-" + nullAble + "-" + remarks + "-" + columnDef + "-" + sqlDataType + "-" + sqlDatetimeSub   
                        + charOctetLength + "-" + ordinalPosition + "-" + isNullAble + "-" + isAutoincrement + "-");   
            }   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 获得一个表的索引信息  
     */  
    public void getIndexInfo(String schemaName, String tableName)   
    {   
        try  
        {   
            ResultSet rs = dbMetaData.getIndexInfo(null, schemaName, tableName, true, true);   
            while (rs.next())   
            {   
                // Can index values be non-unique. false when TYPE is   
                // tableIndexStatistic   
                boolean nonUnique = rs.getBoolean("NON_UNIQUE");   
                // index catalog (may be null); null when TYPE is   
                // tableIndexStatistic   
                String indexQualifier = rs.getString("INDEX_QUALIFIER");   
                // index name; null when TYPE is tableIndexStatistic   
                String indexName = rs.getString("INDEX_NAME");   
                // index type:   
                // tableIndexStatistic - this identifies table statistics that   
                // are returned in conjuction with a table's index descriptions   
                // tableIndexClustered - this is a clustered index   
                // tableIndexHashed - this is a hashed index   
                // tableIndexOther - this is some other style of index   
                short type = rs.getShort("TYPE");   
                // column sequence number within index; zero when TYPE is   
                // tableIndexStatistic   
                short ordinalPosition = rs.getShort("ORDINAL_POSITION");   
                // column name; null when TYPE is tableIndexStatistic   
                String columnName = rs.getString("COLUMN_NAME");   
                // column sort sequence, "A" => ascending, "D" => descending,   
                // may be null if sort sequence is not supported; null when TYPE   
                // is tableIndexStatistic   
                String ascOrDesc = rs.getString("ASC_OR_DESC");   
                // When TYPE is tableIndexStatistic, then this is the number of   
                // rows in the table; otherwise, it is the number of unique   
                // values in the index.   
                int cardinality = rs.getInt("CARDINALITY");   
                System.out.println(nonUnique + "-" + indexQualifier + "-" + indexName + "-" + type + "-"  
                        + ordinalPosition + "-" + columnName + "-" + ascOrDesc + "-" + cardinality);   
            }   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 获得一个表的主键信息  
     */  
    public void getAllPrimaryKeys(String schemaName, String tableName)   
    {   
        try  
        {   
            ResultSet rs = dbMetaData.getPrimaryKeys(null, schemaName, tableName);   
            while (rs.next())   
            {   
                // column name   
                String columnName = rs.getString("COLUMN_NAME");   
                // sequence number within primary key( a value of 1 represents   
                // the first column of the primary key, a value of 2 would   
                // represent the second column within the primary key).   
                short keySeq = rs.getShort("KEY_SEQ");   
                // primary key name (may be null)   
                String pkName = rs.getString("PK_NAME");   
                System.out.println(columnName + "-" + keySeq + "-" + pkName);   
            }   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 获得一个表的外键信息  
     */  
    public void getAllImportedKeys(String schemaName, String tableName)   
    {   
        try  
        {   
//            ResultSet rs = dbMetaData.getExportedKeys(null, schemaName, tableName);   
            ResultSet rs = dbMetaData.getImportedKeys(null, schemaName, tableName);   

            while (rs.next())   
            {   
                // primary key table catalog (may be null)   
                String pkTableCat = rs.getString("PKTABLE_CAT");   
                // primary key table schema (may be null)   
                String pkTableSchem = rs.getString("PKTABLE_SCHEM");   
                // primary key table name   
                String pkTableName = rs.getString("PKTABLE_NAME");   
                // primary key column name   
                String pkColumnName = rs.getString("PKCOLUMN_NAME");   
                // foreign key table catalog (may be null) being exported (may   
                // be null)   
                String fkTableCat = rs.getString("FKTABLE_CAT");   
                // foreign key table schema (may be null) being exported (may be   
                // null)   
                String fkTableSchem = rs.getString("FKTABLE_SCHEM");   
                // foreign key table name being exported   
                String fkTableName = rs.getString("FKTABLE_NAME");   
                // foreign key column name being exported   
                String fkColumnName = rs.getString("FKCOLUMN_NAME");   
                // sequence number within foreign key( a value of 1 represents   
                // the first column of the foreign key, a value of 2 would   
                // represent the second column within the foreign key).   
                short keySeq = rs.getShort("KEY_SEQ");   
                // What happens to foreign key when primary is updated:   
                // importedNoAction - do not allow update of primary key if it   
                // has been imported   
                // importedKeyCascade - change imported key to agree with   
                // primary key update   
                // importedKeySetNull - change imported key to NULL if its   
                // primary key has been updated   
                // importedKeySetDefault - change imported key to default values   
                // if its primary key has been updated   
                // importedKeyRestrict - same as importedKeyNoAction (for ODBC   
                // 2.x compatibility)   
                short updateRule = rs.getShort("UPDATE_RULE");   
  
                // What happens to the foreign key when primary is deleted.   
                // importedKeyNoAction - do not allow delete of primary key if   
                // it has been imported   
                // importedKeyCascade - delete rows that import a deleted key   
                // importedKeySetNull - change imported key to NULL if its   
                // primary key has been deleted   
                // importedKeyRestrict - same as importedKeyNoAction (for ODBC   
                // 2.x compatibility)   
                // importedKeySetDefault - change imported key to default if its   
                // primary key has been deleted   
                short delRule = rs.getShort("DELETE_RULE");   
                // foreign key name (may be null)   
                String fkName = rs.getString("FK_NAME");   
                // primary key name (may be null)   
                String pkName = rs.getString("PK_NAME");   
                // can the evaluation of foreign key constraints be deferred   
                // until commit   
                // importedKeyInitiallyDeferred - see SQL92 for definition   
                // importedKeyInitiallyImmediate - see SQL92 for definition   
                // importedKeyNotDeferrable - see SQL92 for definition   
                short deferRability = rs.getShort("DEFERRABILITY");   
                System.out.println(pkTableCat + "-" + pkTableSchem + "-" + pkTableName + "-" + pkColumnName + "-"  
                        + fkTableCat + "-" + fkTableSchem + "-" + fkTableName + "-" + fkColumnName + "-" + keySeq + "-"  
                        + updateRule + "-" + delRule + "-" + fkName + "-" + pkName + "-" + deferRability);   
            }   
        } catch (SQLException e)   
        {   
            // TODO: handle SQLException   
            e.printStackTrace();   
        }   
    }   
}  