/*
 * Field.java
 *
 * Created on 19. September 2003, 14:26
 */

package de.cismet.cids.admin.importAnt;

/** Bean zur Beschreibung eines Feldes einer Zeiltabelle
 * @author hell
 */
public class FieldDescription {
    /** Logger */    
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    
    /** Tabellenname */
    private String tableName;
    
    /** Feldname */
    private String fieldName;
    
    /** Konstruktor der die Bean anlegt (Tabellenname,Feldname)
     * @param table Tabellenname
     * @param field Feldname
     */
    public FieldDescription(String table, String field) {
        tableName=table;
        fieldName=field;
    }
    
    /** Getter for property tableName.
     * @return Value of property tableName.
     *
     */
    public String getTableName() {
        return this.tableName;
    }
    
    /** Setter for property tableName.
     * @param tableName New value of property tableName.
     *
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    /** Getter for property fieldName.
     * @return Value of property fieldName.
     *
     */
    public String getFieldName() {
        return this.fieldName;
    }
    
    /** Setter for property fieldName.
     * @param fieldName New value of property fieldName.
     *
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    /** Stringrepr\u00E4sentation der Bean FieldDescription
     * @return Stringwert der Bean
     */    
    public String toString() {
        return tableName+"."+fieldName;
    }
    
    /** \u00DCberladene Equals - Methode
     * @param o zu vergleichendes Objekt
     * @return true wenn gleich
     * false sonst
     */    
    public boolean equals(Object o) {
        FieldDescription tmp;
        try {
            tmp=(FieldDescription)o;
        }
        catch (Exception e) {
            return false;
        }
        if (tableName.equals(tmp.getTableName())&&fieldName.equals(tmp.getFieldName())) {
            return true;
        }
        else {
            return false;
        }
    
    }
    
    /** \u00DCberladene HashCode Methode der Bean
     * Wird ben\u00F6tigt, da die Bean Key in HashMaps sein kann
     * @return Hash-Wert der Bean
     */    
    public int hashCode() {
       return toString().hashCode();
    }
    
    
}
