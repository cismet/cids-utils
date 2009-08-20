/*
 * IntermedTablesContainer.java
 *
 * Created on 19. September 2003, 13:31
 */
package de.cismet.cids.admin.importAnt;

import java.util.*;
import java.sql.Connection;

/** Diese Klasse stellt einen Container f\u00FCr alle IntermedTables dar und bietet
 * Methoden zum F\u00FCllen an.
 * @author hell
 */
public class IntermedTablesContainer {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    /** Container f\u00FCr dei IntermedTables */
    HashMap<String, IntermedTable> tables;
    /** Metainformationsobjekt */
    ImportMetaInfo metaInf;
    /** Creates a new instance of IntermedTablesContainer
     * @param metaInf Metainformationsobjekt
     */
    Connection targetConn;

    public IntermedTablesContainer(ImportMetaInfo metaInf, Connection targetConn) throws WrongNameException {
        this.metaInf = metaInf;
        this.targetConn = targetConn;
        tables = new LinkedHashMap<String, IntermedTable>();
        Iterator<String> tabNames = metaInf.getTargetTableNames();
        while (tabNames.hasNext()) {
            String tabName = tabNames.next();
            log.debug("-----------Anlegen von IntermedTables");
            log.debug("-tabName:" + tabName);
            log.debug("-getTargetFieldsAsStringArray:" + getStARR(metaInf.getTargetFieldsAsStringArray(tabName)));
            log.debug("-getTargetEnclosingCharsAsStringArray:" + getStARR(metaInf.getTargetEnclosingCharsAsStringArray(tabName)) + "[" + metaInf.getTargetEnclosingCharsAsStringArray(tabName).length + "]");
            log.debug("-targetConn:" + targetConn);
            log.debug("-----------Anlegen von IntermedTables fertig ------");
            IntermedTable tab = new IntermedTable(tabName, metaInf.getTargetFieldsAsStringArray(tabName), metaInf.getTargetEnclosingCharsAsStringArray(tabName), targetConn);
            tables.put(tabName, tab);
        }
        log.debug("IntermedTablesContainer:\n" + tables);
    }

    public ImportMetaInfo getMetaInfo() {
        return metaInf;
    }

    /** F\u00FCgt die \u00FCbergebenen Daten in die tempor\u00E4ren Tabellen ein. <br>
     * Beachtet wird dabei: Normalisierung, automatische Inkrementierung,
     * Relationenmanagement
     * @param rowData Daten die eingef\u00FCgt werden
     * @throws CidsImportAntException wenn Fehler
     */
    public void addCurrentRows(LinkedHashMap<String, String[]> rowData) throws CidsImportAntException {
        //log.fatal("vorher\n"+getRowData(rowData));
        Vector<String> seq = metaInf.getTableWithPathSequence();
        int thisKeyFieldNo = -1;
        for (int i = 0; i < seq.size(); ++i) {
            String tabN = seq.get(i);
            String keyValue = "";

            String thisKeyField = null;
            //ID Feld herausfinden (WrongNameException wenn keine Master-Tabelle existiert)
            try {
                thisKeyField = metaInf.getDetailKeyField(tabN);
                thisKeyFieldNo = metaInf.getDetailedKeyFieldNo(tabN);
            } catch (WrongNameException wne) {
                log.debug("Nicht so tragische WrongNameException" + tabN, wne);
                //Nicht so tragisch. Weiter im Takt ;-)
                //thisKeyField=null zeigt an dass keine MasterTabelle existiert
                thisKeyField = null;
                thisKeyFieldNo = -1;
            }



            //Normalize
            boolean normalizedRow = false;
            if (metaInf.shouldBeNormalized(tabN)) {
                //Checken ob der Datensatz schon vorhanden ist
                int[] fieldNosWhichAreNeededForComparison = metaInf.getRelevantFieldNosForComparing(tabN);
                String[] values = rowData.get(tabN);
                if (values == null) {
                    throw new WrongNameException(tabN);
                }
                String[] result = getIntermedTable(tabN).searchForRow(fieldNosWhichAreNeededForComparison, values, true);

                if (result != null) {
                    // Treffer
                    normalizedRow = true;
                    //kein autoInkrement
                    // Schl\u00FCssel abspeichern und weiter
                    if (thisKeyFieldNo != -1) {
                        keyValue = result[thisKeyFieldNo];
                    }

                    //Schmeiss die Zeile raus
                    rowData.remove(tabN);
                }
            }
            if (!normalizedRow) {
                //AutoInkrement
                try {
                    int[] autoInc = metaInf.getAutoIncFieldNos(tabN);
                    //log.debug("Es gibt "+autoInc.length+" autoInc Felder");
                    //if (autoInc!=null) {
                    // Es m\u00FCssen Felder hochgez\u00E4hlt werden
                    // finde den vorg\u00E4ngerwert und addiere 1
                    // schreibe diesen wert anschliessend in die entspr. spalte
                    for (int j = 0; j < autoInc.length; ++j) {
                        //log.debug("-"+tabN);
                        //log.debug("-"+getIntermedTable(tabN));

                        try {
                            String val = getIntermedTable(tabN).getLatestValue(autoInc[j]);

                            //uggly debugged
                            //if (val.equals("")) val="0";

                            int integ;
                            try {
                                integ = new Integer(val).intValue();
                                integ++;
                                //setValueInRowData(rowData, tabN, 0, new Integer(integ).toString());
                                setValueInRowData(rowData, tabN, autoInc[j], new Integer(integ).toString());
                            } catch (NumberFormatException nex) {
                                throw new CidsImportAntException("Kein Zahlenformat in einer Auto Inkrement Spalte!(" + val + ")");
                            }
                        } catch (NoValuesException e) {

                            //this.setValueInRowData(rowData, tabN, 0, new Integer("0").toString());
                            this.setValueInRowData(rowData, tabN, autoInc[j], new Integer("0").toString());
                        }
                    }
                } catch (WrongNameException wrne) {
                //Es war keine AutoInkrementSpalte vorhanden: nicht so schlimm, weiter gehts
                }
            }

            //RelationManagement wird nur f\u00FCr Tabellen gemacht die DetailTabelle sind
            log.debug("VOR RELATION MANAGEMENT");
            if (thisKeyField != null) {
                if (!normalizedRow) {
                    log.debug("RELATION MANAGEMENT: KEY");
                    log.debug(rowData);
                    log.debug(tabN);
                    log.debug(new Integer(metaInf.getDetailedKeyFieldNo(tabN)));


                    keyValue = getValueInRowData(rowData, tabN, metaInf.getDetailedKeyFieldNo(tabN));
                    log.debug(keyValue);
                }
                ////RelationManagement
                String[] forTabs = metaInf.getForeignTablesWithPath(tabN, thisKeyField);

                log.debug(this.getStARR(forTabs));
                if (forTabs != null) {
                    for (int j = 0; j < forTabs.length; ++j) {
                        if (isTheRightForeignTable(tabN, forTabs[j])) {
                            String s = metaInf.getForeignField(tabN, thisKeyField, forTabs[j]);
                            int storeOffset = metaInf.getForeignFieldNo(tabN, thisKeyField, forTabs[j]);
                            log.debug("Speichern in Feld:" + tabN + "." + s);
                            //Speichern des Wertes im aktuellen Zwischenspeicher
                            setValueInRowData(rowData, forTabs[j], storeOffset, keyValue);
                        }
                    }
                }
            }
        }
        //log.fatal("nachher\n"+getRowData(rowData));
        // Hier werden die Ergebnisse in die IntermedTables geschrieben
        Iterator<String> tableIt = rowData.keySet().iterator();
        while (tableIt.hasNext()) {
            boolean breakIt = false;
            String tabNam = tableIt.next();
            //Testen ob in dieser Tabelle ein Crossreference angelegt wurde
            String[] fields = (String[]) rowData.get(tabNam);
            for (int i = 0; i < fields.length; ++i) {
                if (fields[i] != null && fields[i].startsWith(ImportMetaInfo.CROSS_REF_PREFIX)) {
                    try {
                        String referenceTablename = fields[i].substring(fields[i].indexOf(ImportMetaInfo.CROSS_REF_PREFIX) + ImportMetaInfo.CROSS_REF_PREFIX.length(), fields[i].indexOf("."));
                        String referenceFieldname = fields[i].substring(fields[i].lastIndexOf(".") + 1, fields[i].length());
                        log.debug("referenceTablename:" + referenceTablename);
                        log.debug("referencFieldname:" + referenceFieldname);
                        log.debug("rowData" + rowData);
                        String referenceValue = getValueInRowData(rowData, referenceTablename, metaInf.getPositionInTable(ImportMetaInfo.getPureTabName(referenceTablename), referenceFieldname));

                        log.debug("referenceValue:" + referenceValue);

                        setValueInRowData(rowData, tabNam, i, referenceValue);
                    } catch (Exception e) {
                        //Crossreferencing ging schief
                        log.error("Crossreferencing ging schief: " + e);
                        log.debug("Crossreferencing ging schief", e);

                    }
                } else if (fields[i] != null && fields[i].trim().equals(ImportMetaInfo.BREAK_IDENTIFIER)) {
                    breakIt = true;
                    break;
                }
            }

            IntermedTable tmp = getIntermedTable(tabNam);
            if (tmp != null && breakIt == false) {
                tmp.addRow((String[]) rowData.get(tabNam));
            }
        }
    }

    private boolean isTheRightForeignTable(String detailTable, String foreignTable) {
        String dt = detailTable;
        String ft = foreignTable;

        if (ft.indexOf("$FROM") == -1) {
            log.debug("Foreign Table(" + foreignTable + ") ohne $FROM");
            return true;
        }

        try {
            dt = dt.substring(dt.indexOf("$FROM") + 5);
            ft = ft.substring(ft.indexOf("$FROM") + 5);
        } catch (Exception e) {
            return false;
        }
        log.debug(dt + "?=" + ft + " ???");
        return dt.startsWith(ft);
    }

    /** Methode zum Setzen eines Wertes in einer bestimmten InterMedTable
     * @param rowData Hastable Container
     * @param tabname Zieltabellenname
     * @param pos Spaltennummer
     * @param value Wert
     */
    private void setValueInRowData(LinkedHashMap<String, String[]> rowData, String tabname, int pos, String value) {
        (rowData.get(tabname))[pos] = value;
    }

    /** Methode zur Ausgabe eines Wertes in einer bestimmten InterMedTable
     * @param rowData Hastable Container
     * @param tabname Zieltabellenname
     * @param pos Spaltennummer
     * @return Wert
     */
    private String getValueInRowData(LinkedHashMap<String, String[]> rowData, String tabname, int pos) {
        return ((rowData.get(tabname)))[pos];
    }

    /** Methode zur Ausgabe einer IntermedTable aus der LinkedHashMap
     * @param tabNa Tabellenname
     * @throws WrongNameException wird geworfen wenn der Tabellenname falsch ist.
     * @return IntermedTable
     */
    public IntermedTable getIntermedTable(String tabNa) throws WrongNameException {
        tabNa = ImportMetaInfo.getPureTabName(tabNa);
        IntermedTable iTab = tables.get(tabNa);
        if (tabNa == null) {
            throw new WrongNameException("Malformed TableName !!!");
        } else {
            return iTab;
        }
    }

    /** Erzeugt eine Stringrepr\u00E4sentation von einem IntermedTablesContainer-Objekt
     * @return Stringrepr\u00E4sentation des IntermedTablesContainer-Objekts
     */
    public String toString() {
        return this.tables.toString();
    }

    /**
     *
     */
    public int getNumberOfTargetTables() {

        return tables.size();
    }

    public Iterator<IntermedTable> getIntermedTablesIterator() {
        return tables.values().iterator();
    }

    public Connection getTargetDBConnection() {
        return targetConn;
    }

    /** DebugMethode
     * @param s StringArray
     */
    public void printStARR(String[] s) {
        if (s == null) {
            log.debug("S[]=NULL");
        } else {
            StringBuffer sB = new StringBuffer("");
            for (int i = 0; i < s.length; ++i) {
                sB.append(s[i] + ",");
            }
            log.debug("S[]=" + sB.toString());
        }
    }

    /** DebugMethode
     * @param s StringArray
     */
    public String getStARR(String[] s) {
        if (s == null) {
            return "NULL";
        } else {
            StringBuffer sB = new StringBuffer("");
            for (int i = 0; i < s.length; ++i) {
                sB.append(s[i] + ",");
            }
            return sB.toString();
        }
    }

    public String getRowData(LinkedHashMap<String, String[]> rowData) {
        String ret = "";
        Iterator<String> it = rowData.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            ret += key + "-->" + getStARR((String[]) rowData.get(key)) + "\n";
        }
        return ret;
    }
}
