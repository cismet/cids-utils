/*
 * gh.java
 *
 * Created on 27. Oktober 2003, 10:55
 */

package de.cismet.cids.admin.importAnt.finalizer;
import de.cismet.cids.admin.importAnt.*;
import java.io.*;
/** Klasse zur Ausgabe der Daten in Importdateien
 *
 * @author  hell
 */
public class SapDBImportFilesGenerator extends Finalizer{
    
    /** Logger */    
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    
    /** Holds value of property outputDirectory. */
    private String outputDirectory;
    
    /** Holds value of property filePrefix. */
    private String filePrefix;
    
    /** Holds value of property delimiter. */
    private String delimiter;
    
    /** Holds value of property seperator. */
    private String seperator;
    
    /** Creates a new instance of SapDBImportFilesGenerator */
    public SapDBImportFilesGenerator() {
        log.debug("Konstruktoraufruf");
    }
    
    /** Setter for property outputDirectory.
     * @param outputDirectory New value of property outputDirectory.
     *
     */
    public void setOutputDirectory(String outputDirectory) {
        log.debug("setOutputDirectory");
        this.outputDirectory = outputDirectory;
    }    
    
    /** Setter for property filePrefix.
     * @param fielPrefix New value of property fielPrefix.
     *
     */
    public void setFilePrefix(String filePrefix) {
        log.debug("setFilePrefix");
        this.filePrefix = filePrefix;
    }    
    
    /** Setter for property delimiter.
     * @param delimiter New value of property delimiter.
     *
     */
    public void setDelimiter(String delimiter) {
        log.debug("setDelimiter");
        this.delimiter = delimiter;
    }
    
    /** Setter for property seperator.
     * @param seperator New value of property seperator.
     *
     */
    public void setSeperator(String seperator) {
        log.debug("setSeperator");
        this.seperator = seperator;
    }
    
    /** Die Methode die die ganze Arbeit erledigt ;-)
     *
     */
    public void finalise() throws Exception{
        log.debug("finalise");
        for (int i=0; i<intermedTables.getNumberOfTargetTables();++i) {
            String tableName=(String)intermedTables.getMetaInfo().getTableSequence().elementAt(i);
            FileWriter fw=new FileWriter(outputDirectory+filePrefix+"_"+tableName+".data");
            IntermedTable itab=intermedTables.getIntermedTable(tableName);
            fw.write(itab.getTableString(false, seperator, delimiter));
            fw.close();
        }
        FileWriter fw=new FileWriter(outputDirectory+filePrefix+"_importControl.sql");
        fw.write("SET TIMESTAMP ISO\n");
        fw.write("//\n");
        fw.write("SET COMPRESSED '/"+seperator+"/"+delimiter+"/'\n");
        fw.write("//\n");
        for (int i=0; i<intermedTables.getNumberOfTargetTables();++i) {
            String tableName=(String)intermedTables.getMetaInfo().getTableSequence().elementAt(i);
            IntermedTable itab=intermedTables.getIntermedTable(tableName);
            fw.write("DATALOAD TABLE "+tableName+"\n");
            for (int j=0; j<itab.getColumnCount();++j) {
                fw.write("  "+itab.getColumnName(j)+"\t"+new Integer(j+1)+"\n");
            }
            fw.write("INFILE '"+outputDirectory+filePrefix+"_"+tableName+".data'\n");
            fw.write("//\n");
        }
        fw.write("COMMIT\n");
        fw.close();
    }
}
