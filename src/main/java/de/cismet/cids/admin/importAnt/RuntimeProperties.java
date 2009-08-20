/*
 * RuntimeProperties.java
 *
 * Created on 30. Oktober 2003, 09:58
 */

package de.cismet.cids.admin.importAnt;

/**
 *
 * @author  hell
 */
public class RuntimeProperties {
    
    /** Holds value of property tmpDirectory. */
    private String tmpDirectory;
    
    /** Holds value of property finalizerClass. */
    private String finalizerClass;
    
    /** Holds value of property finalizerProperties. */
    private java.util.Properties finalizerProperties;
    
    /** Holds value of property importFileName. */
    private String importFileName;
    
    
    private boolean keepAssigner;
    
    /** Creates a new instance of RuntimeProperties */
    public RuntimeProperties() {
    }
    
    /** Getter for property tmpDirectory.
     * @return Value of property tmpDirectory.
     *
     */
    public String getTmpDirectory() {
        if (!tmpDirectory.equals("")) {
            return this.tmpDirectory;
        }
        else {
            String tempDir=System.getProperty("java.io.tmpdir");
            String fileSep=System.getProperty("file.separator");
            if (!tempDir.substring(tempDir.length()-1).equals(fileSep)) {
                tempDir=tempDir+fileSep;
            }
            return tempDir;
        }
    }
    
    /** Setter for property tmpDirectory.
     * @param tmpDirectory New value of property tmpDirectory.
     *
     */
    public void setTmpDirectory(String tmpDirectory) {
        this.tmpDirectory = tmpDirectory;
    }
    
    /** Getter for property finsiherClass.
     * @return Value of property finsiherClass.
     *
     */
    public String getFinalizerClass() {
        return this.finalizerClass;
    }
    
    /** Setter for property finsiherClass.
     * @param finsiherClass New value of property finsiherClass.
     *
     */
    public void setFinalizerClass(String finalizerClass) {
        this.finalizerClass = finalizerClass;
    }
    
    /** Getter for property finalizerProperties.
     * @return Value of property finalizerProperties.
     *
     */
    public java.util.Properties getFinalizerProperties() {
        return this.finalizerProperties;
    }
    
    /** Setter for property finalizerProperties.
     * @param finalizerProperties New value of property finalizerProperties.
     *
     */
    public void setFinalizerProperties(java.util.Properties finalizerProperties) {
        this.finalizerProperties = finalizerProperties;
    }
    
    /** Getter for property importFile.
     * @return Value of property importFile.
     *
     */
    public String getImportFileName() {
        return this.importFileName;
    }
    
    /** Setter for property importFile.
     * @param importFile New value of property importFile.
     *
     */
    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName;
    }
    
    /** Getter for property keepAssigner.
     * @return Value of property keepAssigner.
     *
     */
    public boolean isKeepAssigner() {
        return keepAssigner;
    }
    
    /** Setter for property keepAssigner.
     * @param keepAssigner New value of property keepAssigner.
     *
     */
    public void setKeepAssigner(boolean keepAssigner) {
        this.keepAssigner = keepAssigner;
    }
    
}
