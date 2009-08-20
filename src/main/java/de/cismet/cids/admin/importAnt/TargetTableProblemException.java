/*
 * TargetTableProblemException.java
 *
 * Created on 26. September 2003, 11:18
 */

package de.cismet.cids.admin.importAnt;

/** Wird geworfen wenn im Zielsystem eine Tabelle nicht \u00FCbereinstimmt
 * @author hell
 */
public class TargetTableProblemException extends CidsImportAntException {
    
    /** Creates a new instance of TargetTableProblemException
     * @param s Beschreibung der Exception
     */
    public TargetTableProblemException(String s) {
        super(s);
    }
    
}
