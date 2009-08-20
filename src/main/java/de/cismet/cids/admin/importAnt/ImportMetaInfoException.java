/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */

package de.cismet.cids.admin.importAnt;

/** Wird geworfen, wenn beim Anlegen der Metainformation ein Fehler auftritt.
 * @author hell
 */
public class ImportMetaInfoException extends CidsImportAntException {
    
    /** Creates a new instance of ImportMetaInfoException */
    public ImportMetaInfoException() {
    }
    /** Creates a new instance of ImportMetaInfoException
     * @param s Beschreibung der Exception
     */    
    public ImportMetaInfoException(String s) {
        super(s);
    }
    /** Creates a new instance of ImportMetaInfoException
     * @param s Beschreibung der Exception
     */    
    public ImportMetaInfoException(String s,Throwable cause) {
        super(s,cause);
    }

}
