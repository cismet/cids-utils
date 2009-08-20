/*
 * JDBCImportExecutor.java
 *
 * Created on 28. Oktober 2003, 11:40
 */

package de.cismet.cids.admin.importAnt.finalizer;
import de.cismet.cids.admin.importAnt.*;
import java.sql.*;
import java.io.*;
/**
 *
 * @author  hell
 */
public class SQLScriptGenerator extends Finalizer{
    public static final int MAX_LOG_ERROR=20;
    /** Logger */    
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    /** Holds value of property rollback. */
    
    /** Creates a new instance of JDBCImportExecutor */
    public SQLScriptGenerator() {
    }
    
    
    /** Die Methode die die ganze Arbeit erledigt ;-)
     *
     */
    public long finalise() throws Exception{
        log.debug("finalise");
        long errorCounter=0;
        String stmnt;
        FileWriter fw=new FileWriter("C:\\temp\\out.sql");

        for (int i=0; i<intermedTables.getNumberOfTargetTables();++i) {
            String tableName=(String)intermedTables.getMetaInfo().getTableSequence().elementAt(i);
            log.info("Import in Tabelle: " + tableName+"\n");
            logs+="\nImport in Tabelle:" +tableName+"\n\n";
            IntermedTable itab=intermedTables.getIntermedTable(tableName);

            int logErrorCounter=0;

            for (int j=0; j<itab.getRowCount();++j) { 
                setProgressValue(tableName, j+1);
                stmnt=getFixedPartOfInsertStatement(itab)+getValuesForInsert(itab,j);
                fw.write(stmnt+"\n");
                fw.write("//\n");
            }
        }
        log.info("Import abgeschlossen");
        logs+="\n\n-----------------Import abgeschlossen";
        fw.flush();
        fw.close();
        return errorCounter;
     }
    
     protected String getValuesForInsert(IntermedTable itab, int position) throws CidsImportAntException{
        return "("+itab.getRowStringWithGivenEnclosingChar(position, ",")+")";
     }
    
     protected String getFixedPartOfInsertStatement(IntermedTable itab) throws CidsImportAntException{
        return "INSERT INTO " + itab.getTableName() +"("+getFieldList(itab)+") VALUES";
     }
    
     protected String getFieldList(IntermedTable itab) throws CidsImportAntException{
        StringBuffer sBuff=new StringBuffer(""); 
        for (int i=0;i<itab.getColumnCount()-1;++i) {
            sBuff.append(itab.getColumnName(i)).append(",");
        }
        sBuff.append(itab.getColumnName(itab.getColumnCount()-1));
        return sBuff.toString();
    }
}
