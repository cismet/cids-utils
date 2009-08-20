/*
 * CidsImportAntException.java
 *
 * Created on 22. September 2003, 11:09
 */

package de.cismet.cids.admin.importAnt;

/** Gefangene Exceptions in diesem Package sind alle von diesem Typ
 * @author hell
 */
public class CidsImportAntException extends java.lang.Exception {
    
    /** Creates a new instance of CidsImportAntException */
    public CidsImportAntException() {
    }
    
    /** Zweiter Konstruktor der einen String \u00FCbergibt
     * @param s String Wert der Exception
     */    
    public CidsImportAntException(String s) {
        super(s);
    }
    /** Dritter Konstruktor der einen String und eine Exception \u00FCbergibt
     * @param s String Wert der Exception
     * @param e Zus\u00E4tzliche Exception
     */    
    public CidsImportAntException(String s, Throwable e) {
        super(s,e);
    }
    
}
