/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */

package de.cismet.cids.admin.importAnt;

/** Wird geworfen wenn beim Initialisieren was schief geht, viele reflection fehler
 *
 * @author hell
 */
public class InitializingException extends CidsImportAntException {
    private String initializeLog="";    
    /** Creates a new instance of InitializingException */
    public InitializingException() {
    }
    /** Creates a new instance of InitializingException
     * @param s Beschreibung der Exception
     */    
    public InitializingException(String s,String log) {
        super(s);
        initializeLog=log;
    }
    
    public InitializingException(String s,String log, Throwable e) {
        super(s,e);
        initializeLog=log;
    }

    /** Getter for property initializeLog.
     * @return Value of property initializeLog.
     *
     */
    public java.lang.String getInitializeLog() {
        return initializeLog;
    } 
    
    
}
