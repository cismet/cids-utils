/*
 * JDBCImportExecutor.java
 *
 * Created on 28. Oktober 2003, 11:40
 */

package de.cismet.cids.admin.importAnt.finalizer;
import de.cismet.cids.admin.importAnt.*;
import java.sql.*;
/**
 *
 * @author  hell
 */
public class JDBCImportExecutor extends Finalizer{
    public static final int MAX_LOG_ERROR=20;
    /** Logger */    
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    /** Holds value of property rollback. */
    private String rollback;
    boolean rb;
    
    /** Creates a new instance of JDBCImportExecutor */
    public JDBCImportExecutor() {
    }
    
    /** Setter for property rollback.
     * @param rollback New value of property rollback.
     *
     */
    public void setRollback(String rollback) {
        // :-(
        // GEFAHR
        this.rollback = rollback;
        if (rollback.equals("true")) {
            log.info("Rollback wurde auf true gesetzt. Das Einfuegen wird wieder rueckgaengig gemacht!");
            rb=true;
        }
        else {
            rb=false;
        }
    }
    
    
    /** Die Methode die die ganze Arbeit erledigt ;-)
     *
     */
    public long finalise() throws Exception{
        log.debug("finalise");
        long errorCounter=0;
        String stmnt;
        Connection conn=intermedTables.getTargetDBConnection();
        if (rb) {
            conn.setAutoCommit(false);
        }
        else {
            conn.setAutoCommit(true);
        }
        for (int i=0; i<intermedTables.getNumberOfTargetTables();++i) {
            String tableName=intermedTables.getMetaInfo().getTableSequence().elementAt(i);
            
            log.info("Import in Tabelle: " + tableName+" ("+intermedTables.getIntermedTable(tableName).getRowCount()+" Datensaetze)\n");
            logs+="\nImport in Tabelle:" +" ("+intermedTables.getIntermedTable(tableName).getRowCount()+" Datensaetze)\n\n";
            IntermedTable itab=intermedTables.getIntermedTable(tableName);

            int logErrorCounter=0;
           

            for (int j=0; j<itab.getRowCount();++j) { 
                setProgressValue(tableName, j+1,logErrorCounter);
                stmnt=getFixedPartOfInsertStatement(itab)+getValuesForInsert(itab,j);
                Statement s=conn.createStatement();
                try {
                    s.execute(stmnt);
                    s.close();
                } catch (SQLException ex) {
                    errorCounter++;
                    logErrorCounter++;
                    log.error("Fehler bei:"+stmnt+": "+ex);
                    log.debug("Fehler bei:"+stmnt,ex);
                    setProgressValue(tableName, j+1,logErrorCounter);

                    if (logErrorCounter<MAX_LOG_ERROR) {
                        logs+="    Fehler beim Import .. Statement:"+stmnt+" ("+ex.toString()+")\n";
                    } else if (logErrorCounter==MAX_LOG_ERROR) {
                        logs+="    ************** mehr Fehler (Abbruch der Ausgabe)\n";
                    }
                }
                try {
                    if (rb) conn.rollback();
                    //.execute("ROLLBACK");
                } catch (SQLException ex) {
                    logErrorCounter++;
                    log.error("Fehler bei:ROLLBACK: "+ex);
                    log.debug("Fehler bei:ROLLBACK",ex);
                    if (logErrorCounter<MAX_LOG_ERROR) {
                        logs+="    Fehler beim Import .. Rollback des Statement:"+stmnt+" ("+ex.toString()+")\n";
                    } else if (logErrorCounter==MAX_LOG_ERROR) {
                        logs+="    ************** mehr Fehler (Abbruch der Ausgabe)\n";
                    }
                }
            }
        }
        
        conn.close(); // todo: check
        log.info("Import abgeschlossen");
        logs+="\n\n-----------------Import abgeschlossen";
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
