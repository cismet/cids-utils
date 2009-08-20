/*
 * Finisher.java
 *
 * Created on 27. Oktober 2003, 11:11
 */

package de.cismet.cids.admin.importAnt;
import java.util.Properties;


/**
 *
 * @author  hell
 */
public abstract class Finisher {
    
    /** Holds value of property intermedTables. */
    protected IntermedTablesContainer intermedTables; 
    
    
    /** Creates a new instance of Finisher */
    public Finisher() {
    }
    
    
    /** Setter for property intermedTables.
     * @param intermedTables New value of property intermedTables.
     *
     */
    public void setIntermedTables(IntermedTablesContainer intermedTables) {
        this.intermedTables = intermedTables;
    }
    
    
}
