/*
 * IntermedTable.java
 *
 * Created on 17. September 2003, 13:16
 */
package de.cismet.cids.admin.importAnt;

import java.util.*;
import java.sql.*;

/** Diese Klasse dient als Zwischenspeicher f\u00FCr eine Zieltabelle.
 * @author hell
 */
public class IntermedTable extends javax.swing.table.DefaultTableModel {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    /** Tabellenname der Zieltabelle */
    private String tableName;
    /** Container der die Daten aufnimmt */
    private Vector[] table;
    /** Container der Daten speichert die schon einmal in der Datenbank gefunden wurden*/
    private Vector[] foundInDbTable;
    /** Spaltenname */
    private String[] columnNames;
    /**Enclosing Characters*/
    private String[] enclChars;
    /** LinkedHashMap in der alle Spaltennamen einer Spaltennummer zugeordnet sind */
    private LinkedHashMap<String, Integer> columnNameToNumberMapping;
    /** Anzahl der Spalten */
    private int columnCount;
    /**Connection zum Zielsystem*/
    Connection targetConn;
    private int doubleInc = 0; //mit dieser Variable wird das Problem der Mehrfachzugriffe bei MultiRelations und AutoInkrement gel\u00F6st

    /** Creates a new instance of IntermedTable
     * @param enclChars Array der Zeichen die bei einem Vergleich oder Insert um den jeweiligen String
     * kommen. In der Regel ' oder Blank
     * @param targetConn Connection zum Zielsystem
     * @param tableName Tabellenname der Zieltabelle
     * @param targetFields Spaletnnamen der Zieltabelle
     */
    public IntermedTable(String tableName, String[] targetFields, String[] enclChars, Connection targetConn) {
        columnCount = targetFields.length;
        columnNames = targetFields;
        this.tableName = tableName;
        this.targetConn = targetConn;

        table = new Vector[columnCount];
        for (int i = 0; i < columnCount; ++i) {
            table[i] = new Vector();
        }

        foundInDbTable = new Vector[columnCount];
        for (int i = 0; i < columnCount; ++i) {
            foundInDbTable[i] = new Vector();
        }

        setColumnNameToNumberMapping(targetFields);
        log.debug("setColumnNameToNumberMapping(targetFields):" + this.columnNameToNumberMapping);
        this.enclChars = enclChars;
    }

    /** Liefert die Zelleninformation an der \u00FCbergebenden Stelle der Zieltabelle
     * @param column Spaltennummer
     * @param row Zeilennummer
     * @throws NoValuesException Exception wird geworfen wenn keine Daten an entsprechender Stelle evorhanden sind
     * @return Wert der Zelle als String
     */
    public String getValue(int column, int row) throws NoValuesException {
        if (table[column].size() == 0) {
            throw new NoValuesException();
        }
        if (table[column].get(row) != null) {
            return table[column].get(row).toString();
        } else {
            //TODO DANGER
            //return "";
            return null;
        }
    }

    /** Liefert den neuesten Wert in der angegebenen Spalte
     * @param column Spaltennummer
     * @throws NoValuesException NoValuesException Exception wird geworfen wenn keine Daten an entsprechender Stell evorhanden sind
     * @return Wert der Zelle als String
     */
    public String getLatestValue(int column) throws NoValuesException {
        //log.fatal("getLatestValue["+column+"]   ");
        //log.fatal("table[column]="+table[column]);
        if (table[column] == null || table[column].size() == 0) {
            // Die Datenstruktur ist leer
            // In der Datenbank nachschauen
            try {
                log.debug("getLatestValue:kuckt in der DB");
                Statement s = targetConn.createStatement();
                String stmnt = "Select max(" + this.columnNames[column] + "), count(*) from " + tableName;
                log.debug(stmnt);
                ResultSet r = s.executeQuery(stmnt);
                r.next();
                int count = r.getInt(2);

                if (count == 0 && doubleInc == 0) {
                    doubleInc++;
                    //throw new NoValuesException("keine Werte in der Datenstruktur oder der Datenbank");
                    return "0";
                } else if (count == 0 && doubleInc > 0) {
                    String ret2 = new Integer(doubleInc).toString();
                    doubleInc++;
                    return ret2;
                }

                String ret = r.getString(1);
                if (ret == null && doubleInc == 0) {
                    doubleInc++;
                    //throw new NoValuesException("keine Werte in der Datenstruktur oder der Datenbank");
                    return "0";

                }
                ret = stringInc(ret, doubleInc);
                doubleInc++;
                return ret;
            } catch (java.sql.SQLException sqlEx) {
                doubleInc++;
                log.fatal("AutoValue", sqlEx);
                //throw new NoValuesException("Fehler beim Nachschauen nach dem MaxWert in der DB");
                return "0";
            }

        }
        String ret = getValue(column, table[column].size() - 1);
        ret = stringInc(ret, doubleInc);
        doubleInc++;
        return ret;
    }

    private String stringInc(String s, int inc) {
        if (inc == 0) {
            return s;
        }
        try {
            Integer i = new Integer(s);
            return new Integer(i.intValue() + inc).toString();
        } catch (Exception e) {
            log.error("Fehler bei der String-Inkrementierung: " + e);
            log.debug("Fehler bei der String-Inkrementierung", e);
            return "0";
        }
    }

    /** Liefert die Zelleninformation an der \u00FCbergebenden Stelle der Zieltabelle
     * @param column Spaltenname
     * @param row Zeilennummer
     * @throws NoValuesException Exception wird geworfen wenn keine Daten an entsprechender Stelle evorhanden sind
     * @throws WrongNameException Exception wird geworfen wenn Spaltenname nicht vorhanden ist
     * @return Wert der Zelle als String
     */
    public String getValue(String column, int row) throws NoValuesException, WrongNameException {
        int cNo;
        try {
            Integer columnNo = columnNameToNumberMapping.get(column);
            cNo = columnNo.intValue();
        } catch (Exception e) {
            throw new WrongNameException("Wrong columnName " + column);
        }
        if (table[cNo].size() == 0) {
            throw new NoValuesException();
        }
        return getValue(cNo, row);
    }

    /** Liefert den neuesten Wert in der angegebenen Spalte
     * @param column Spaltenname
     * @throws NoValuesException NoValuesException Exception wird geworfen wenn keine Daten an entsprechender Stell evorhanden sind
     * @throws WrongNameException Exception wird geworfen wenn Spaltenname nicht vorhanden ist
     * @return Wert der Zelle als String
     */
    public String getLatestValue(String column) throws NoValuesException, WrongNameException {
        int cNo;
        try {
            Integer columnNo = columnNameToNumberMapping.get(column);
            cNo = columnNo.intValue();
        } catch (Exception e) {
            throw new WrongNameException("Wrong columnName " + column);
        }
        if (table[cNo].size() == 0) {
            return "0";
        //throw new NoValuesException();
        }
        return getLatestValue(cNo);
    }

    /** Erzeugt eine Stringrepr\u00E4sentation der IntermedTable
     * @return Stringrepr\u00E4sentation der IntermedTable
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("\n\n");

        buf.append(tableName + "\n");
        buf.append("...............................\n");
        for (int i = 0; i < columnCount - 1; ++i) {
            buf.append(columnNames[i]).append(", ");
        }
        buf.append(columnNames[columnCount - 1]).append("\n");
        buf.append("--------------------------------------------------------\n");
        try {
            buf.append(getTableString(false, ",", ""));
        } catch (NoValuesException e) {
            buf.append("-----------------");
        }
        return buf.toString();
    }

    public int getcolumnCount() {
        return columnCount;
    }

    /** F\u00FCgt der IntermedTable eine neue Zeile an
     * @param rowData String[] der neuen Zeile
     * @throws CidsImportAntException wird geworfen falls die L\u00E4nge des
     * \u00FCbergebenen Arrays nicht mit der Anzahl der
     * Spalten der Zieltabelle \u00FCbereinstimmt
     */
    public void addRow(String[] rowData) throws CidsImportAntException {
        doubleInc = 0;
        addRow(rowData, table);
    }

    /** F\u00FCgt einem Vector[] eine neue Zeile an
     * @param rowData String[] der neuen Zeile
     * @param container Vector[] in dem die Daten gespeichert werden
     * @throws CidsImportAntException wird geworfen falls die L\u00E4nge des
     * \u00FCbergebenen Arrays nicht mit der Anzahl der
     * Spalten der Zieltabelle \u00FCbereinstimmt
     */
    private void addRow(String[] rowData, Vector[] container) throws CidsImportAntException {
        if (rowData.length == columnCount) {
            for (int i = 0; i < columnCount; ++i) {
                container[i].add(rowData[i]);
            }
        } else {
            throw new CidsImportAntException("RowData is not matching columnCount");
        }
    }

    /** Liefert eine Stringrepr\u00E4sentation einer Zeile der Zieltabelle
     * @param rowNo Zeilennummer
     * @param sep String mit dem die einzelnen Felder getrennt werden (z.B ",")
     * @param deli String mit dem die einzelnen Felder eingeschlossen werden (z.B. "\"")
     * @throws NoValuesException wird geworfen falls keine Daten an dieser Stelle vorhanden sind
     * @return Stringwert der \u00FCbergebenen Zeile
     */
    public String getRowString(int rowNo, String sep, String deli) throws NoValuesException {
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < columnCount - 1; ++i) {
            buf.append(deli);
            buf.append(getValue(i, rowNo));
            buf.append(deli);
            buf.append(sep);
        }
        buf.append(deli);
        buf.append(getValue(columnCount - 1, rowNo));
        buf.append(deli);
        return buf.toString();
    }

    public String getRowStringWithGivenEnclosingChar(int rowNo, String sep) throws NoValuesException {
        StringBuffer buf = new StringBuffer("");
        String eChar;
        for (int i = 0; i < columnCount - 1; ++i) {
            eChar = enclChars[i];
            if (eChar == null) {
                eChar = "";
            }
            buf.append(eChar);
            buf.append(getValue(i, rowNo));
            buf.append(eChar);
            buf.append(sep);
        }
        eChar = enclChars[columnCount - 1];
        if (eChar == null) {
            eChar = "";
        }
        buf.append(eChar);
        buf.append(getValue(columnCount - 1, rowNo));
        buf.append(eChar);
        return buf.toString();
    }

    /** Liefert eine parametrisierbare Stringrepr\u00E4sentation der Zieltabelle
     * @param header wenn true, dann werden die Spaltennamen in die erste zeile ausgegeben
     * @param sep String mit dem die einzelnen Felder getrennt werden (z.B ",")
     * @param deli String mit dem die einzelnen Felder eingeschlossen werden (z.B. "\"")
     * @throws NoValuesException wird geworfen falls keine Daten an dieser Stelle vorhanden sind
     * @return Stringwert der Tabelle
     */
    public String getTableString(boolean header, String sep, String deli) throws NoValuesException {
        StringBuffer buf = new StringBuffer("");
        if (header) {
            for (int i = 0; i < columnCount - 1; ++i) {
                buf.append(deli).append(columnNames[i]).append(deli).append(sep);
            }
            buf.append(deli).append(columnNames[columnCount - 1]).append(deli).append("\n");
        }
        for (int i = 0; i < table[0].size(); ++i) {
            buf.append(getRowString(i, sep, deli)).append("\n");
        }
        return buf.toString();
    }

    /** F\u00FCllt die Hashtabel columnNameToNumberMapping
     * @param columnNames String[] der Spaltennamen
     */
    private void setColumnNameToNumberMapping(String[] columnNames) {
        for (int i = 0; i < columnNames.length; ++i) {
            columnNameToNumberMapping = new LinkedHashMap();
            columnNameToNumberMapping.put(columnNames[i], new Integer(i));
        }
    }

    /** \u00DCberpr\u00FCft ob der \u00DCbergebene Datensatz schon in der Datenstruktur vorkommt.
     * Entscheidende Methode zur Realisierung der Normalisierungsfunktionalit\u00E4t.
     * Diese Methode untersucht nur die Hauptspeicherstruktur.
     * Es werden nur Spalten zum Vergleich herangezogen die in fields[] angegeben sind.
     * @return Falls eine \u00DCbereinstimmung gefunden wurde wird die entsprechende
     * Zeile zur\u00FCckgegeben, um z.B. die entsprechende ID zu erhalten.
     * @param container Gibt an in welchem Container gesucht wird
     * @param fields int[] indem die Spaltennummern angegeben werden die zum Vergleich
     * herangezogen werden
     * @param data <B>komplette</B> Zeile die \u00FCberpr\u00FCft werden soll
     */
    private String[] searchForRow(int[] fields, String[] data, Vector[] container) {
        if (container[0] == null) {
            return null;
        }
        for (int i = 0; i < container[0].size(); ++i) {
            boolean newRow = false;
            for (int j = 0; j < fields.length; ++j) {
//            Object a=data[fields[j]];
//            Object b=container[fields[j]];
//
//            if (a==null || b==null) {
//                log.debug("ugly bug fix");
//                return null;
//            }
                if (!data[fields[j]].equals(container[fields[j]].get(i))) {
                    newRow = true;
                    break;
                }
            }
            if (!newRow) {
                // Alles klar eine ganze zeile hat \u00FCbereingestimmt
                String[] ret = new String[container.length];
                String out = "";

                for (int k = 0; k < ret.length; ++k) {
                    out += container[k].get(i).toString();
                    ret[k] = container[k].get(i).toString();
                }
                return ret;
            }
        }
        return null;
    }

    /** \u00DCberpr\u00FCft ob der \u00DCbergebene Datensatz schon in der Datenstruktur vorkommt.
     * Entscheidende Methode zur Realisierung der Normalisierungsfunktionalit\u00E4t.
     * Diese Methode untersucht nur die Hauptspeicherstruktur.
     * Es werden nur Spalten zum Vergleich herangezogen die in fields[] angegeben sind.
     * @param fields int[] indem die Spaltennummern angegeben werden die zum Vergleich
     * herangezogen werden
     * @param data <B>komplette</B> Zeile die \u00FCberpr\u00FCft werden soll
     * @param alsoInDatabase ist dieses Flag auf true gesetzt, wird nicht nur der momentane Import-
     * datenbestand f\u00FCr einen Vergleich \u00FCberpr\u00FCft, sondern auch die echte
     * Zieltabelle (Datenbank) \u00FCberpr\u00FCft.
     * @return Falls eine \u00DCbereinstimmung gefunden wurde wird die entsprechende
     * Zeile zur\u00FCckgegeben, um z.B. die entsprechende ID zu erhalten.
     * 
     * Genutzt vom IntermedTablesContainer.
     */
    public String[] searchForRow(int[] fields, String[] data, boolean alsoInDatabase) {
        //Datensatz im Hauptspeicher suchen
        String[] ret;
        ret = searchForRow(fields, data, table);
        if (alsoInDatabase && ret == null) {
            //Wurde der Datensatz schon in der Datenbank gefunden ?

            ret = searchForRow(fields, data, foundInDbTable);
            if (ret != null) {
                //log.debug("Was in foundInDbTable gefunden");
                return ret;
            }
            //Datensatz in der Datenbank suchen
            try {
                ret = searchForRowInTargetDb(fields, data);
                if (ret != null) {
                    //log.debug("Was in DB gefunden");
                    return ret;
                }
            } catch (CidsImportAntException ex) {
                //erst mal nix machen
                log.error("Fehler beim Normalisieren auf der DB: " + ex);
                log.debug("Fehler beim Normalisieren auf der DB", ex);
            }
        }
        return ret;
    }

    /** \u00DCberpr\u00FCft ob der \u00DCbergebene Datensatz schon in der Datenbank vorkommt.
     * Entscheidende Methode zur Realisierung der Normalisierungsfunktionalit\u00E4t.
     * Diese Methode untersucht nur die datenbank
     * Es werden nur Spalten zum Vergleich herangezogen die in fields[] angegeben sind.
     * @param fields int[] indem die Spaltennummern angegeben werden die zum Vergleich
     * herangezogen werden
     * @param data <B>komplette</B> Zeile die \u00FCberpr\u00FCft werden soll
     * @throws CidsImportAntException wird geworfen, wenn ein Fehler auftritt (DB, ...)
     * @return Falls eine \u00DCbereinstimmung gefunden wurde wird die entsprechende
     * Zeile zur\u00FCckgegeben, um z.B. die entsprechende ID zu erhalten.
     */
    private String[] searchForRowInTargetDb(int[] fields, String[] data) throws CidsImportAntException {
        String compStmnt = getDBCompareStmnt(fields, data);
        log.debug("getDBCompareStmnt():" + compStmnt);
        try {
            Statement s = targetConn.createStatement();

            ResultSet result = s.executeQuery(compStmnt);
            if (!result.next()) {
                // nix gefunden
                //log.debug("nix gefunden");
                result.close();
                s.close();
                return null;
            } else {
                //was gefunden
                String[] ret = new String[data.length];
                for (int i = 1; i <= data.length; ++i) {
                    ret[i - 1] = result.getString(i);
                }
                //Bevor jetzt die Zeile zur\u00FCckgegeben wird, wird sie noch in
                //foundInDbTable abgespeichert, damit sie sp\u00E4ter schneller gefunden wird
                result.close();
                s.close();
                addRow(ret, foundInDbTable);
                log.debug("Was gefunden:" + ret[0]);
                return ret;
            }
        } catch (java.sql.SQLException sqlEx) {
            throw new CidsImportAntException(sqlEx.toString());
        }
    }

    /** Liefert das SQL-Statement mit dem \u00FCberpr\u00FCft wird, ob ein Datensatz schon in der
     * Datenbank steht. Wird zum normalisieren verwendet.
     * @param fields int[] das die Felder angibt, die zum Vergleich herangezogen werden
     * @param data alle Daten dieses Datensatzes in einem String[]
     * @return SQL-Statement mit dem \u00FCberpr\u00FCft wird, ob ein Datensatz schon in der
     * Datenbank steht
     */
    private String getDBCompareStmnt(int[] fields, String[] data) {
        //Statement zum \u00DCberpr\u00FCfen im Zielsystem wird nun zusammengebaut
        StringBuffer testingStatementBuf = new StringBuffer("");
        testingStatementBuf.append("select ");
        for (int i = 0; i < data.length - 1; ++i) {
            //testingStatementBuf.append(this.columnNames[fields[i]]+",");
            testingStatementBuf.append(this.columnNames[i] + ",");
        }
        testingStatementBuf.append(this.columnNames[data.length - 1]);

        testingStatementBuf.append(" from ").append(tableName).append(" where ");
        for (int i = 0; i < fields.length - 1; ++i) {
            // Hier Fallunterscheidung ob mit enclosingChars oder nicht
            String eChar = enclChars[fields[i]];
            if (eChar == null) {
                eChar = "";
            }
            testingStatementBuf.append(this.columnNames[fields[i]]).append("=").append(eChar).append(data[fields[i]]).append(eChar).append(" and ");
        }
        String eChar = enclChars[fields[fields.length - 1]];
        if (eChar == null) {
            eChar = "";
        }
        testingStatementBuf.append(this.columnNames[fields[fields.length - 1]]).append("=").append(eChar).append(data[fields[fields.length - 1]]).append(eChar);
        ;
        String testingStatement = testingStatementBuf.toString();
        //log.debug(testingStatement);
        return testingStatement;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        if (table == null) {
            return 0;
        } else {
            return table[0].size();
        }
    }

    @Override
    public String getColumnName(int columnNo) {
        return columnNames[columnNo];
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        try {
            return getValue(column, row);
        } catch (NoValuesException nve) {
            log.error("Fehler in getValueAt --> Tablemodel IntermedTable:" + nve);
            log.debug("Fehler in getValueAt --> Tablemodel IntermedTable", nve);
            return null;
        }
    }
}
