/*
 * ImportMetaInfo.java
 *
 * Created on 16. September 2003, 15:06
 */
package de.cismet.cids.admin.importAnt;

import de.cismet.cids.admin.importAnt.castorGenerated.*;
import java.util.*;
import java.sql.*;

/** importMetaInfo h\u00E4lt die notwendigen Metainformationen zu einem Import.
 * Dazu geh\u00F6ren neben dem Zugriffsobjekt der Importdate auch noch weitere
 * Convenience Funktionen.
 * @author hell
 */
public class ImportMetaInfo {

    public static final String CROSS_REF_PREFIX = "-->CIDS-CROSS-REFERENCE:";
    public static final String BREAK_IDENTIFIER = "CIDS-BREAK";
    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private static final org.apache.log4j.Logger logS = org.apache.log4j.Logger.getLogger("de.cismet.cids.admin.importAnt.ImportMetaInfo");
    /** Spaltennamen der Datenquelle */
    private Vector<String> sourceFields = new Vector<String>();
    /** Zieltabellen */
    private HashMap<String, Vector<String>> targetTables = new LinkedHashMap<String, Vector<String>>();
    /** Enclosing Character der einzelnen Felder der Zieltabellen */
    private HashMap<String, LinkedHashMap<String, String>> enclosingChars = new LinkedHashMap<String, LinkedHashMap<String, String>>();
    /** Felder die als automatische Z\u00E4hlfelder realisiert werden sollen */
    private HashMap<String, Vector<String>> autoIncFields = new LinkedHashMap<String, Vector<String>>();
    /** RelationenContainer */
    private HashMap<FieldDescription, Vector<FieldDescription>> relationsHM = new LinkedHashMap<FieldDescription, Vector<FieldDescription>>();
    /** Normalisierungscontainer */
    private HashMap<String, String> normalizeHM = new LinkedHashMap<String, String>();
    /** Mastertabellen */
    private HashMap<String, String> tableIsMaster = new LinkedHashMap<String, String>();
    /** Detailtabellen */
    private HashMap<String, String> tableIsDetail = new LinkedHashMap<String, String>();
    /** Reihenfolge der Abarbeitung der Zieltabellen */
    private Vector<String> tableSequence = new Vector<String>();
    /** Welche Felder sind f\u00FCr einen Vergelich (Normalisierung) relevant ? */
    private HashMap<String, Vector<String>> compareRelevant = new LinkedHashMap<String, Vector<String>>();
    /** CastorObjekt der ImportKonfigurationsDatei (XML) */
    private ImportRules rules;
    private HashMap<String, Vector<String>> tableWithPaths = new LinkedHashMap<String, Vector<String>>();

    /** Konstruiert die Metainformationen aus der entsprechenden Konfigurationsdatei
     * @param rules CastorObjekt der ImportKonfigurationsDatei (XML)
     * @throws CidsImportAntException wird geworfen wenn ein schwerer Fehler auftritt ;-)
     */
    public ImportMetaInfo(ImportRules rules) throws ImportMetaInfoException {
        this.rules = rules;
        try {
            //sourcefields werden nicht im Constructor gesetzt !!!

            //Hinzuf\u00FCgen der Target Fields zu den einzelnen Tabellen
            Enumeration<Mapping> mappings = rules.getPreProcessingAndMapping().enumerateMapping();
            while (mappings.hasMoreElements()) {
                Mapping m = mappings.nextElement();
                String tableName = m.getTargetTable();
                if (!targetTables.containsKey(tableName)) {
                    Vector fields = new Vector();
                    fields.add(m.getTargetField());
                    targetTables.put(tableName, fields);
                } else {
                    Vector<String> v = (targetTables.get(tableName));
                    if (!v.contains(m.getTargetField())) {
                        v.add(m.getTargetField());
                    }
                }
                if (m.getAutoIncrement()) {
                    if (!autoIncFields.containsKey(tableName)) {
                        Vector<String> fields = new Vector<String>();
                        fields.add(m.getTargetField());
                        autoIncFields.put(tableName, fields);
                    } else {
                        Vector<String> v = ((autoIncFields.get(tableName)));
                        if (!v.contains(m.getTargetField())) {
                            v.add(m.getTargetField());
                        }
                    }
                }

                if (m.getComparing()) {
                    if (!compareRelevant.containsKey(tableName)) {
                        Vector<String> fields = new Vector<String>();
                        fields.add(m.getTargetField());
                        compareRelevant.put(tableName, fields);
                    } else {
                        Vector<String> v = ((compareRelevant.get(tableName)));
                        if (!v.contains(m.getTargetField())) {
                            v.add(m.getTargetField());
                        }
                    }
                }
                String eChar = m.getEnclosingChar();
                if (eChar == null) {
                    eChar = "";
                }
                if (!enclosingChars.containsKey(tableName)) {

                    LinkedHashMap<String, String> fields = new LinkedHashMap<String,String>();
                    fields.put(m.getTargetField(), eChar);
                    enclosingChars.put(tableName, fields);
                } else {
                    LinkedHashMap<String, String> lhm = ((enclosingChars.get(tableName)));
                    //if (!v.contains(eChar)) {
                    lhm.put(m.getTargetField(), eChar);
                //}
                }


                if (m.getPath() != null && !m.getPath().equals("")) {
                    if (!tableWithPaths.containsKey(tableName)) {
                        Vector paths = new Vector();
                        paths.add(tableName + getDollarFromAppenderFromPath(m.getPath()));
                        tableWithPaths.put(tableName, paths);
                    } else {
                        Vector<String> v = ((tableWithPaths.get(tableName)));
                        if (!v.contains(tableName + getDollarFromAppenderFromPath(m.getPath()))) {
                            v.add(tableName + getDollarFromAppenderFromPath(m.getPath()));
                        }
                    }
                }
            }




            log.debug("Mappings angelegt");
            log.debug("Pfade:\n" + tableWithPaths);
            //Analysieren des Relations-Abschnitt
            if (rules.getRelations() != null) {
                Enumeration<Relation> relations = rules.getRelations().enumerateRelation();
                while (relations.hasMoreElements()) {
                    Relation r = relations.nextElement();
                    String tableName = r.getMasterTable();

                    //Hinzuf\u00FCgen der Foreign Keys Fields zu den einzelnen Tabellen
                    if (!targetTables.containsKey(tableName)) {
                        throw new CidsImportAntException("Foreign-Key in a non existant Table (" + tableName + ")!!!");
                    } else {
                        Vector<String> v = ((targetTables.get(tableName)));
                        if (!v.contains(r.getMasterTableForeignKey())) {
                            v.add(r.getMasterTableForeignKey());
                        }
                    }

                    //F\u00FCllen der RelationsHashMap
                    FieldDescription f = new FieldDescription(r.getDetailTable().trim(), r.getDetailTableKey().trim());
                    if (!relationsHM.containsKey(f)) {
                        Vector<FieldDescription> foreignFields = new Vector();
                        foreignFields.add(new FieldDescription(r.getMasterTable(), r.getMasterTableForeignKey()));
                        relationsHM.put(f, foreignFields);
                    } else {
                        Vector<FieldDescription> v = ((relationsHM.get(f)));
                        if (!v.contains(new FieldDescription(r.getMasterTable(), r.getMasterTableForeignKey()))) {
                            v.add(new FieldDescription(r.getMasterTable(), r.getMasterTableForeignKey()));
                        }
                    }

                    //Rollen der Tabllen abspeichern
                    tableIsMaster.put(r.getMasterTable(), "-");//r.getMasterTableForeignKey());
                    tableIsDetail.put(r.getDetailTable(), r.getDetailTableKey());


                    if (r.getForeignKeyComparing()) {
                        if (!compareRelevant.containsKey(r.getMasterTable())) {
                            Vector fields = new Vector();
                            fields.add(r.getMasterTableForeignKey());
                            compareRelevant.put(r.getMasterTable(), fields);
                        } else {
                            Vector<String> v = ((compareRelevant.get(r.getMasterTable())));
                            if (!v.contains(r.getMasterTableForeignKey())) {
                                v.add(r.getMasterTableForeignKey());
                            }
                        }
                    }

                    String eChar = r.getEnclosingChar();
                    if (eChar == null) {
                        eChar = "";
                    }
                    if (!enclosingChars.containsKey(tableName)) {
                        LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
                        fields.put(r.getMasterTableForeignKey(), eChar);
                        enclosingChars.put(tableName, fields);
                    } else {
                        HashMap<String, String> lhm = ((enclosingChars.get(tableName)));
                        //if (!v.contains(eChar)) {
                        lhm.put(r.getMasterTableForeignKey(), eChar);
                    //}
                    }
                }
            }

            log.debug("Relations angelegt");
            log.debug(relationsHM);


            if (rules.getOptions() != null) {
                Enumeration<String> normInfo = rules.getOptions().enumerateNormalize();

                while (normInfo.hasMoreElements()) {
                    String n = normInfo.nextElement();
                    normalizeHM.put(n, "!");
                }

            }


            //TableSequence festlegen

            //zuerst kommen die Tabellen, die nur Detail sind
            Iterator<String> it = getTargetTableNames();
            while (it.hasNext()) {
                String tabN = it.next();
                if (!tableIsMaster.containsKey(tabN) && tableIsDetail.containsKey(tabN)) {
                    tableSequence.add(tabN);
                }
            }

            // dann die, die beides sind
            it = getTargetTableNames();
            while (it.hasNext()) {
                String tabN = it.next();
                if (tableIsMaster.containsKey(tabN) && tableIsDetail.containsKey(tabN)) {
                    tableSequence.add(tabN);
                }
            }
            // am schluss kommen die, die nur master sind
            it = getTargetTableNames();
            while (it.hasNext()) {
                String tabN = it.next();
                if (tableIsMaster.containsKey(tabN) && !tableIsDetail.containsKey(tabN)) {
                    tableSequence.add(tabN);
                }
            }

            //haben wir auch keinen vergessen ???
            it = getTargetTableNames();
            while (it.hasNext()) {
                String tabN = it.next();
                if (!tableIsMaster.containsKey(tabN) && !tableIsDetail.containsKey(tabN)) {
                    tableSequence.add(tabN);
                }
            }
        } catch (CidsImportAntException ex) {
            throw new ImportMetaInfoException("Fehler beim Anlegen der Import-Metainformationen.", ex);
        }

    }

    /** Liefert die Felder der Datenquelle
     * @return Vector: Felder der Datenquelle
     */
    public Vector<String> getSourceFields() {
        return sourceFields;
    }

    /** Setzt die Felder der Datenquelle. Analysiert das Source ResultSet und setzt dann
     * die entsprechenden Spaltennamen
     * @param rs ResultSet das analysiert wird
     * @throws CidsImportAntException wird geworfen, wenn ein SQL Fehler auftritt
     */
    public void setSourceFields(ResultSet rs) throws ImportMetaInfoException {
        try {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                sourceFields.add(rs.getMetaData().getColumnLabel(i));
            //sourceFields.add(rs.getMetaData().getColumnName(i));
            }
        } catch (java.sql.SQLException sqlEx) {
            throw new ImportMetaInfoException("Fehler beim Anlegen der Import-Metainformationen (Feldnamen der Datenquelle).", sqlEx);
        }
    }

    /** Liefert einen Iterator der \u00FCber die Ziehltabellennamen l\u00E4uft
     * @return Iterator der \u00FCber die Ziehltabellennamen l\u00E4uft
     */
    public Iterator<String> getTargetTableNames() {
        return targetTables.keySet().iterator();
    }

    /** Liefert einen Iterator der \u00FCber die Ziehltabellennamen mit Pfad l\u00E4uft
     * @return Iterator der \u00FCber die Ziehltabellennamen l\u00E4uft
     */
    public Iterator<String> getTargetTableNamesWithPath() {
        return this.getTableWithPathSequence().iterator();
    }

    /** Liefert die Spaltennamen der \u00FCbergebenen Zieltabelle
     * @return Spaltennamen der \u00FCbergebenen Zieltabelle als Vector
     * @param tableName Zieltabelle
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     *
     */
    public Vector<String> getTargetFields(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<String> ret = (targetTables.get(tableName));
        if (ret == null) {
            throw new WrongNameException("Wrong TableName: " + tableName);
        }
        return ret;
    }

    /** Liefert alle im Konfigurationsfile angegebenen Enclosing Characters f\u00FCr eine
     * bestimmte Tabelle
     * @param tabName Tabellennamen
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return String[]: enclosing Characters f\u00FCr eine bestimmte Tabelle
     */
    public String[] getTargetEnclosingCharsAsStringArray(String tabName) throws WrongNameException {
        tabName = getPureTabName(tabName);
        LinkedHashMap<String, String> lhm = (enclosingChars.get(tabName));
        if (lhm == null) {
            throw new WrongNameException("Wrong TableName:" + tabName);
        }

        Vector<String> v = new Vector<String>();
        Iterator<String> i = lhm.keySet().iterator();
        while (i.hasNext()) {
            String fieldname = i.next();
            v.add(lhm.get(fieldname));
        }
        String[] s = new String[v.size()];


        for (int j = 0; j < s.length; ++j) {
            s[j] = v.get(j);
        }
        return s;


    }

    /** Liefert die Spaltennamen der \u00FCbergebenen Zieltabelle
     * @return Spaltennamen der \u00FCbergebenen Zieltabelle als String[]
     * @param tableName Zieltabelle
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     */
    public String[] getTargetFieldsAsStringArray(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<String> v = getTargetFields(tableName);
        String[] s = new String[v.size()];
        for (int i = 0; i < s.length; ++i) {
            s[i] = v.get(i);
        }
        return s;
    }

    /** Erzeugt aus einem Zieltabellenname ein SQL-Statement. Wenn dieses SQL-Statement
     * (Select .... ) ohne Fehler ausgef\u00FChrt werden kann, existiert die Tablle im
     * Zielsystem und der Import kann durchgef\u00FChrt werden.
     * @return String: SQL-Statement
     * @param tableName Zieltabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     */
    public String getControlStatement(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        StringBuffer stmnt = new StringBuffer("select ");
        Vector<String> fields = (targetTables.get(tableName));
        if (fields != null) {
            Enumeration<String> enu = fields.elements();
            while (enu.hasMoreElements()) {
                String field = enu.nextElement();
                stmnt.append(field);
                if (enu.hasMoreElements()) {
                    stmnt.append(",");
                }
            }
            stmnt.append(" from ").append(tableName);
            stmnt.append(" where 0=1");
            return stmnt.toString();
        } else {
            throw new WrongNameException("Wrong TableName: " + tableName);
        }
    }

    /** Liefert eine Stringrepr\u00E4sentation des Objektes
     * @return Stringrepr\u00E4sentation des Objektes
     */
    public String toString() {
        return targetTables.toString() + "\n\n" + sourceFields.toString();
    }

    /** Erzeugt aus den Metadaten der import-Konfiguration eine Java-Klasse. Diese
     * klasse wird dazu verwendet deo emtsprechenden Felder zuzuweisen und einfache
     * Datenkonvertierungsaufgaben zu \u00FCbernehmen. Sp\u00E4ter im Workflow wird diese Klasse
     * w\u00E4hrend der Laufzeit kompiliert und \u00FCber Reflection aufgerufen.
     *
     *<br>
     * Cool Was ??? ;-)
     * @param filename Dateiname der Zuk\u00FCnftigen Java-Klasse
     * @throws CidsImportAntException wird bei einem Fehler geworfen
     * @return String: Java-Quelltext der Klasse
     */
    public String getPreProcessingAndMappingClass(String filename) throws CidsImportAntException {
        StringBuffer buf = new StringBuffer("");
        buf.append("/**\n * cids ImportAnt mapping class (DO NOT CHANGE)\n");
        buf.append(" * Source created @ " + System.currentTimeMillis() + "\n");
        buf.append(" */\n\n\n");
        buf.append("import java.util.*;\n");
        buf.append("import java.sql.*;\n");
        buf.append("import de.cismet.cids.admin.importAnt.*;\n\n");



        if (rules.getCode() != null && rules.getCode().getImport() != null) {
            buf.append("// user defined imports\n\n");
            buf.append(rules.getCode().getImport() + "\n");
        }
        buf.append(" public class " + filename + " {\n");
        buf.append("    public " + filename + "(){}\n\n");
        buf.append("    private static java.sql.Connection targetConnection=null;\n");
        buf.append("    private static UniversalContainer universalContainer;\n");
        buf.append("    public static LinkedHashMap assign(java.sql.Connection tc,String[] args, UniversalContainer universalC) {\n");
        buf.append("        //init\n");
        buf.append("        targetConnection=tc;\n");
        buf.append("        universalContainer=universalC;\n");


        for (int i = 0; i < getSourceFields().size(); ++i) {
            String fieldName = getSourceFields().get(i);
            buf.append("        String " + fieldName + "=args[" + i + "];\n");
        }
        buf.append("\n");

        // Initialisieren der String Arrays
        Iterator<String> tableNames = getTargetTableNamesWithPath();
        while (tableNames.hasNext()) {
            String tabName = tableNames.next();
            log.debug("getClass: TargetTables: ->" + tabName);
            buf.append("        String[] " + tabName + " = new String[" + this.getFieldCount(tabName) + "];\n");
        }
        buf.append("\n        // Assigning Section\n");


        Enumeration<Mapping> enu = rules.getPreProcessingAndMapping().enumerateMapping();
        while (enu.hasMoreElements()) {
            Mapping m = enu.nextElement();
            if (!m.getAutoIncrement()) {
                buf.append("        " + getScriptVariableName(m.getTargetTable(), m.getTargetField(), m.getPath()) + " = " + m.getContent() + ";" + "//by-cids-error-finder:" + m.getContent() + "\n");
            //buf.append(m.getTargetTable()+"__"+m.getTargetField()+" = " + m.getContent()+";\n");
            }
        }
        //preparing the return value
        buf.append("\n        // Preparing the return value\n");
        buf.append("\n        LinkedHashMap hm=new LinkedHashMap();");
        Iterator<String> tabNames = getTargetTableNamesWithPath();
        while (tabNames.hasNext()) {
            String tName = tabNames.next();
            buf.append("        hm.put(\"" + tName + "\"," + tName + ");\n");
        }
        buf.append("        return hm;\n");

        buf.append("    }\n");

        //User defined Procedures
        buf.append(" // User defined Procedures");

        if (rules.getCode() != null) {
            Function[] funcs = rules.getCode().getFunction();
            for (int i = 0; i < rules.getCode().getFunctionCount(); ++i) {
                buf.append("\n");
                buf.append("public static " + funcs[i].getContent().trim());
                buf.append("\n");
            }
        }
        buf.append("\n");
        //cross reference Funktion

        buf.append("    public static String cidsCrossReference(String name) {\n");
        buf.append("        return \"" + ImportMetaInfo.CROSS_REF_PREFIX + "\"+name;\n");
        buf.append("    }\n");

        // break if Funktion

        buf.append("    public static String cidsBreakIf(boolean breakIt,String defaultValue) {\n");
        buf.append("        if (breakIt) { return \"" + ImportMetaInfo.BREAK_IDENTIFIER + "\";} else { return defaultValue; }\n");
        buf.append("    }\n");



        buf.append("\n}\n");





        return buf.toString();

    }

    /** Methode zur Erzeugung des Java-Codes zum Zugriff auf eine Spalte einer
     * Zieltabelle
     * @param targetTable Zieltabelle
     * @param targetField Zielspalte
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder der Spaltenname nicht stimmt
     * @return Java-Code zum Zugriff auf eine Spalte einer Zieltabelle
     */
    private String getScriptVariableName(String targetTable, String targetField, String path) throws WrongNameException {
        if (path == null) {
            path = "";
        }
        path = getDollarFromAppenderFromPath(path);
        return targetTable + path + "[" + getPositionInTable(targetTable, targetField) + "]";

    }

    /** Liefert den Spaltenindex der Spalte einer Zieltabelle
     * @param tableName Zieltabelle
     * @param fieldName Zielspalte
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder der Spaltenname nicht stimmt
     * @return Spaltenindex
     */
    public int getPositionInTable(String tableName, String fieldName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<String> fields = targetTables.get(tableName);
        if (fields == null) {
            throw new WrongNameException("Malformed Table Name (" + tableName + ")");
        }
        int index = fields.indexOf(fieldName);
        if (index == -1) {
            throw new WrongNameException("Malformed FieldName (" + fieldName + ")");
        }
        return index;
    }

    /** Gibt die Anzahl der Felder einer Zieltabelle aus
     * @param tableName Zieltabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return Anzahl der Felder der Zieltabelle
     */
    private int getFieldCount(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<String> fields = targetTables.get(tableName);
        if (fields == null) {
            throw new WrongNameException("Malformed Table Name");
        }
        return fields.size();
    }

    // Methoden die f\u00FCrs RelationManagement ben\u00F6tigt werden
    /** Gibt einen Vector aus, in dem die Reihenfolge der Zieltabellen mit den Beziehungspfaden abgelegt ist.
     * Die Reihenfolge der Zieltabellen ist nicht frei w\u00E4hlbar, da es Beziehungen
     * zwischen den einzelnen Zieltabellen gibt.
     * @return Vector mit den Namen der Zieltabellen (Strings) in der richtigen Reihenfolge.
     */
    public Vector<String> getTableWithPathSequence() {
        Vector<String> v = new Vector<String>();
        Enumeration<String> e = tableSequence.elements();
        while (e.hasMoreElements()) {
            String tableName = e.nextElement();
            v.addAll(getTablesWithPathFromPureTableName(tableName));
        }
        return v;
    }

    /** Gibt einen Vector aus, in dem die Reihenfolge der Zieltabellen abgelegt ist.
     * Die Reihenfolge der Zieltabellen ist nicht frei w\u00E4hlbar, da es Beziehungen
     * zwischen den einzelnen Zieltabellen gibt.
     * @return Vector mit den Namen der Zieltabellen (Strings) in der richtigen Reihenfolge.
     */
    public Vector<String> getTableSequence() {
        return tableSequence;
    }

    public String[] getForeignTablesWithPath(String tableName, String fieldName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        String[] withoutPath = getForeignTables(tableName, fieldName);
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < withoutPath.length; ++i) {
            v.addAll(this.getTablesWithPathFromPureTableName(withoutPath[i]));
        }
        //Weil aus irgendeinem Grund ein String[] zur\u00FCckgegeben werden muss, wird der Vector jetzt
        //noch umgewandelt. Wer hat das nur progr.? ;-)

        String[] ret = new String[v.size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = v.elementAt(i);
        }
        return ret;
    }

    /** liefert alle Tabellennamen, die das \u00FCbergebene Feld der \u00FCbergebenen Tabelle als
     * Fremdschl\u00FCssel referenzieren.
     * <br>
     * Bsp.:<br>
     * ein <CODE>getForeignTables("Url","UrlID")</CODE> k\u00F6nnte als Ergebniss liefern:
     * <br>
     * <CODE>"Altlast",""Fluss"</CODE> weil beide Tabellen auf die Tabelle Url
     * verweisen und dort ihre Beschreibung ablegen
     * @return alle Tabellennamen, die das \u00FCbergebene Feld der \u00FCbergebenen Tabelle als
     * Fremdschl\u00FCssel referenzieren
     * @param tableName Tabellenname
     * @param fieldName Feldname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder Spaltenname nicht stimmt
     */
    public String[] getForeignTables(String tableName, String fieldName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<FieldDescription> v = relationsHM.get(new FieldDescription(tableName, fieldName));
        if (v == null) {
            throw new WrongNameException("Wrong TableName.FieldName: " + tableName + "." + fieldName);
        }
        String[] arr = new String[v.size()];
        for (int i = 0; i < v.size(); ++i) {
            arr[i] = ((v.get(i))).getTableName();
        }
        return arr;
    }

    /** liefert den Feldnamen des Feldes, das auf das
     * \u00FCbergebene Feld der \u00FCbergebenen Tabelle referenziert (<B>Fremdschl\u00FCssel</B>). Zus\u00E4tzlich muss noch die
     * Tabelle angegeben werden.
     *
     * <br>
     * Bsp.:
     * <br>
     * erster Schritt:
     * <br>
     * ein <CODE>getForeignTables("Url","UrlID")</CODE> k\u00F6nnte als Ergebniss liefern:
     * <br>
     * <CODE>"Altlast",""Fluss"</CODE> weil beide Tabellen auf die Tabelle Url
     * verweisen und dort ihre Beschreibung ablegen
     * <br>
     * wenn man nun <CODE>getForeignField("Url","UrlID","Altlast")</CODE> aufruft,
     * k\u00F6nnte die Methode folgendes Ergebniss liefern:
     *    <CODE>DescrId</CODE>
     * @return Fremdschl\u00FCsselfeldnamen
     * @param tableName Tabellenname der Detailtabelle
     * @param fieldName Feldname des Detailschl\u00FCssels (Prim\u00E4rschl\u00FCssel der Detailtabelle)
     * @param foreignTable Tabellenname der Tabelle die auf die Detailtabelle referenziert
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder Spaletnname nicht stimmt
     */
    public String getForeignField(String tableName, String fieldName, String foreignTable) throws WrongNameException {
//        log.fatal("getForeignField");
//        log.fatal("tableName:"+tableName);
//        log.fatal("fieldName:"+fieldName);
//        log.fatal("foreignTable:"+foreignTable);

        if (tableName.indexOf("$FROM") == -1) {
            tableName = getPureTabName(tableName);

            foreignTable = getPureTabName(foreignTable);
            Vector<FieldDescription> v = relationsHM.get(new FieldDescription(tableName, fieldName));
            if (v == null) {
                throw new WrongNameException("Wrong TableName.FieldName: " + tableName + "." + fieldName);
            }
            for (int i = 0; i < v.size(); ++i) {
                if (((v.get(i))).getTableName().equals(foreignTable)) {
                    return ((v.get(i))).getFieldName();
                }
            }
            throw new WrongNameException("Wrong ForeignTableName:" + foreignTable);
        } else {
            //in tableName steckt am Ende ein Hinweiss auf das richtige Feld
            foreignTable = getPureTabName(foreignTable);

            tableName = tableName.substring(0, tableName.length() - 1);
            String fieldHint = tableName.substring(tableName.lastIndexOf('$') + 1, tableName.length());
            tableName = getPureTabName(tableName);

            foreignTable = getPureTabName(foreignTable);
            Vector<FieldDescription> v = relationsHM.get(new FieldDescription(tableName, fieldName));
            if (v == null) {
                throw new WrongNameException("Wrong TableName.FieldName: " + tableName + "." + fieldName);
            }
            log.debug(foreignTable + "." + fieldHint);
            for (int i = 0; i < v.size(); ++i) {
                if ((((v.get(i))).getTableName().compareToIgnoreCase(foreignTable) == 0) && (((v.get(i))).getFieldName().compareToIgnoreCase(fieldHint) == 0)) {
                    return ((v.get(i))).getFieldName();
                }
            }
            throw new WrongNameException("Wrong ForeignTableName:" + foreignTable);

        }
    }

    /** liefert die Nummer des Feldes, das auf das
     * \u00FCbergebene Feld der \u00FCbergebenen Tabelle referenziert (<B>Fremdschl\u00FCssel</B>). Zus\u00E4tzlich muss noch die
     * Tabelle angegeben werden.
     *
     *<br>
     * Bsp.:
     *<br>
     * erster Schritt:
     *<br>
     * ein <CODE>getForeignTables("Url","UrlID")</CODE> k\u00F6nnte als Ergebniss liefern:
     *<br>
     * <CODE>"Altlast",""Fluss"</CODE> weil beide Tabellen auf die Tabelle Url
     * verweisen und dort ihre Beschreibung ablegen
     *<br>
     * wenn man nun <CODE>getForeignField("Url","UrlID","Altlast")</CODE> aufruft,
     * k\u00F6nnte die Methode folgendes Ergebniss liefern:
     *    <CODE>DescrId</CODE>
     * @return Fremdschl\u00FCsselfeldnummer
     * @param tableName Tabellenname der Detailtabelle
     * @param fieldName Feldname des Detailschl\u00FCssels (Prim\u00E4rschl\u00FCssel der Detailtabelle)
     * @param foreignTable Tabellenname der Tabelle die auf die Detailtabelle referenziert
     * @throws WrongNameException wird geworfen, wenn der Tabellenname oder der Spaltenname nicht stimmt
     */
    public int getForeignFieldNo(String tableName, String fieldName, String foreignTable) throws WrongNameException {
        return getPositionInTable(foreignTable, getForeignField(tableName, fieldName, foreignTable));
    }

    /** Gibt an ob das angegebene Feld der angegebenen Tabelle in einer Relation
     * verwendet wird.
     * @param tableName Tabellenname
     * @param fieldName Feldname
     * @return boolean: <CODE>true</CODE> wenn in Relation vorhanden, <CODE>false</CODE> sonst
     */
    public boolean isInRelation(String tableName, String fieldName) {
        tableName = getPureTabName(tableName);
        return relationsHM.containsKey(new FieldDescription(tableName, fieldName));
    }

    /** Liefert die Felder einer Tabelle,  bei denen in der Importkonfiguration
     * festgelegt wurde, dass sie AutoInkrement sind.
     * @param tableName Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return Namen der Autoinkrementfelder in einem Vector
     */
    public String[] getAutoIncFieldNames(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<String> v = autoIncFields.get(tableName);
        if (v == null) {
            throw new WrongNameException(tableName);
        }
        String[] arr = new String[v.size()];
        for (int i = 0; i < v.size(); ++i) {
            arr[i] = v.get(i);
        }
        return arr;
    }

    /** Liefert die Feldnummern einer Tabelle,  bei denen in der Importkonfiguration
     * festgelegt wurde, dass sie AutoInkrement sind.
     * @param tableName Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return int[] mit den Feldnummern der AutoInkrementfelder
     */
    public int[] getAutoIncFieldNos(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<String> v = autoIncFields.get(tableName);
        if (v == null) {
            throw new WrongNameException(tableName);
        }
        int[] arr = new int[v.size()];
        for (int i = 0; i < v.size(); ++i) {
            arr[i] = getPositionInTable(tableName, v.get(i));
        }
        return arr;
    }

    /** Gibt die Feldnamen an, die f\u00FCr das Vergleichen beim Normalisieren notwendig
     * sind
     * @param tableName Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return Vector mit den Feldnamen, die f\u00FCr das Vergleichen beim Normalisieren noptwendig
     * sind
     */
    public String[] getRelevantFieldNamesForComparing(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<String> v = compareRelevant.get(tableName);
        if (v == null) {
            throw new WrongNameException(tableName);
        }
        String[] arr = new String[v.size()];
        for (int i = 0; i < v.size(); ++i) {
            arr[i] = v.get(i);
        }
        return arr;
    }

    /** Gibt die Feldnummern an, die f\u00FCr das Vergleichen beim Normalisieren notwendig
     * sind
     * @param tableName Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return int[] mit den Feldnummern, die f\u00FCr das Vergleichen beim Normalisieren noptwendig
     * sind
     */
    public int[] getRelevantFieldNosForComparing(String tableName) throws WrongNameException {
        tableName = getPureTabName(tableName);
        Vector<String> v = compareRelevant.get(tableName);
        if (v == null) {
            throw new WrongNameException(tableName);
        }
        int[] arr = new int[v.size()];
        for (int i = 0; i < v.size(); ++i) {
            arr[i] = getPositionInTable(tableName, v.get(i));
        }
        return arr;
    }

    /** Gibt an ob eine Tabelle normalisiert werden soll, oder nicht. Beim normalisieren
     * werden doppelte Datens\u00E4tze identifiziert und durch Verwenduing der
     * entsprechenden vorhandenen Datens\u00E4tze wird Speicherplatz gespart.
     * <br>
     * (Erkl\u00E4rung f\u00FCr Laien)
     * @param tableName Tabellenname
     * @return <CODE>true</CODE> wenn Tabelle normalisiert werden soll, <CODE>false</CODE> sonst
     */
    public boolean shouldBeNormalized(String tableName) {
        tableName = getPureTabName(tableName);
        return normalizeHM.containsKey(tableName);
    }

    /** Liefert das Schl\u00FCsselfeld in einer Detail-Tabelle.
     * <br>
     * :-( maybe buggy
     * <br>
     * z.B.:
     * <CODE>Url_ID</CODE> in der Tabelle <CODE>Url</CODE>
     * @return Schl\u00FCsselfeld in einer Detail-Tabelle
     * @param detailTable Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     */
    public String getDetailKeyField(String detailTable) throws WrongNameException {
        detailTable = getPureTabName(detailTable);
        String ret = tableIsDetail.get(detailTable);
        if (ret == null) {
            throw new WrongNameException("Wrong TableName: " + detailTable);
        }
        return ret;
    }

    /** Liefert die Nummer des Schl\u00FCsselfeldes in einer Detail-Tabelle.
     * <br>
     * :-( maybe buggy<br>
     * z.B.:
     * <CODE>Url_ID</CODE> in der Tabelle <CODE>Url</CODE>
     * @param detailTable Tabellenname
     * @throws WrongNameException wird geworfen, wenn der Tabellenname nicht stimmt
     * @return Schl\u00FCsselfeldnummer in einer Detail-Tabelle
     */
    public int getDetailedKeyFieldNo(String detailTable) throws WrongNameException {
        detailTable = getPureTabName(detailTable);
        return getPositionInTable(detailTable, getDetailKeyField(detailTable));
    }

    /**
     *Entfernt das $FROM$relationPath aus dem Tabellennamen
     */
    public static String getPureTabName(String tabName) {
        String ret = "";
        int i = tabName.indexOf("$FROM$");
        if (i < 0) {
            ret = tabName;
        } else {
            ret = tabName.substring(0, i);
        }
        logS.debug("getPureTabname(" + tabName + ")=" + ret);
        return ret;
    }


    /*
     *Erzeugt aus dem Beziehungspfad das $FROM$-Format zur Verwendung in Variablennamen
     *
     */
    private String getDollarFromAppenderFromPath(String path) {
        if (path == null || path.equals("")) {
            return "";
        } else {
            path = path.replace('/', '$');
            path = path.replace('.', '$');
            return "$FROM" + path;
        }
    }

    private Vector<String> getTablesWithPathFromPureTableName(String tableName) {
        Vector<String> v = new Vector<String>();
        tableName = ImportMetaInfo.getPureTabName(tableName);
        Vector<String> paths = tableWithPaths.get(tableName);
        if (paths == null) {
            v.add(tableName);
        } else {
            Enumeration<String> e = paths.elements();
            while (e.hasMoreElements()) {
                v.add(e.nextElement());
            }
        }
        return v;
    }
}
