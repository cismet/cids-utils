/*
 * NoValuesException.java
 *
 * Created on 22. September 2003, 10:59
 */

package de.cismet.cids.admin.importAnt;

/** Wir geworfen wenn kein Wert gefunden wurde
 * @author hell
 */
public class NoValuesException extends CidsImportAntException {
    
    /** Creates a new instance of NoValuesException */
    public NoValuesException() {
    }
    
    /** Creates a new instance of NoValuesException
     * @param s Beschreibung der Exception
     */    
    public NoValuesException(String s) {
        super(s);
    }
    
    
}
