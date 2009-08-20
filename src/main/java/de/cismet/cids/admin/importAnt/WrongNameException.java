/*
 * WrongNameException.java
 *
 * Created on 22. September 2003, 11:02
 */

package de.cismet.cids.admin.importAnt;

/** Wird geworfen wenn wahrscheinlich ein falscher Name gew\u00E4hlt wurde. Meistens dann
 * wenn kein Key in einer HasMap gefunden wurde, er aber da sein m\u00FCsste.
 * <br>
 * m\u00FCsste aber ;-)
 * @author hell
 */
public class WrongNameException extends CidsImportAntException {
    
    /** Creates a new instance of WrongNameException */
    public WrongNameException() {
    }
    /** Creates a new instance of WrongNameException
     * @param s Beschreibung der Exception
     */    
    public WrongNameException(String s) {
        super(s);
    }

}
