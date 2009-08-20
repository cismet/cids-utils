/*
 * Finalizer.java
 * Created on 27. Oktober 2003, 11:11
 */
package de.cismet.cids.admin.importAnt;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.util.Iterator;
import de.cismet.cids.admin.importAnt.castorGenerated.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author  hell
 */
public class Finalizer extends DefaultTableModel {

    protected String logs = "";
    /** Holds value of property intermedTables. */
    protected IntermedTablesContainer intermedTables;
    int rowCount = 0;
    String[] tableNames = null;
    HashMap<String, JProgressBar> progress = new LinkedHashMap<String, JProgressBar>();

    /** Creates a new instance of Finalizer */
    public Finalizer() {
//        //test
//        rowCount=3;
//
//        tableNames=new String[]{"1","2","3"};
//        for (int i=0; i<3;++i) {
//            JProgressBar j=new JProgressBar();
//            j.setMaximum(100);
//            j.setBorderPainted(false);
//            j.setValue((i+1)*20);
//            progress.put(tableNames[i], j);
//        }
    }

    /** Setter for property intermedTables.
     * @param intermedTables New value of property intermedTables.
     *
     */
    public void setIntermedTables(IntermedTablesContainer intermedTables) {
        this.intermedTables = intermedTables;
        rowCount = intermedTables.getNumberOfTargetTables();
        Iterator<IntermedTable> it = intermedTables.getIntermedTablesIterator();
        tableNames = new String[rowCount];
        int i = 0;
        while (it.hasNext()) {
            IntermedTable iTab = it.next();
            tableNames[i] = iTab.getTableName();
            int rowCount = iTab.getRowCount();
            JProgressBar pro = null;
            if (rowCount == 0) {
                rowCount = 1;
                pro = new JProgressBar(0, rowCount);
                pro.setValue(1);
            } else {
                pro = new JProgressBar(0, rowCount);
            }
            pro.setBorderPainted(false);
            progress.put(tableNames[i], pro);
            ++i;
        }
    }

    public void setProgressValue(String tabName, int value, long errors) {

        JProgressBar j = progress.get(tabName);
        j.setStringPainted(true);
        j.setString(value + " (" + errors + " Fehler)");
        j.setValue(value);
        this.fireTableDataChanged();
    //this.fireTableCellUpdated(row, 1);
    }

    public void setProgressValue(String tabName, int value) {

        JProgressBar j = progress.get(tabName);
        j.setStringPainted(false);
        j.setValue(value);
        this.fireTableDataChanged();
    //this.fireTableCellUpdated(row, 1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return tableNames[row];
            case 1:
                return progress.get(tableNames[row]);
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Zieltabelle";
            case 1:
                return "Status";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return java.lang.String.class;
            case 1:
                return javax.swing.JProgressBar.class;
        }
        return Object.class;
    }

    public String getLogs() {
        return logs;
    }
}
