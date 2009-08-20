/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */

package de.cismet.cids.admin.importAnt;

/** Wird geworfen wenn beim "finishen" was schief geht, viele reflection fehler
 *
 * @author hell
 */
public class FinisherException extends CidsImportAntException {
    
    /** Creates a new instance of FinisherException */
    public FinisherException() {
    }
    /** Creates a new instance of FinisherException
     * @param s Beschreibung der Exception
     */    
    public FinisherException(String s) {
        super(s);
    }
    
    public FinisherException(String s, Throwable e) {
        super(s,e);
    }

}
