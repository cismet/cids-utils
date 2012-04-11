/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.common.gui.sqlwizard;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ResultSetTable extends JTable {

    //~ Instance fields --------------------------------------------------------

    private final History history;

    /** Holds value of property maxRows. */
    private int maxRows = 75;

    private final ResultSetTableSorter tableSorter = new ResultSetTableSorter();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ResultSetTable.
     *
     * @param  maxHistorySize  DOCUMENT ME!
     * @param  maxRows         DOCUMENT ME!
     */
    public ResultSetTable(final int maxHistorySize, final int maxRows) {
        super();

        this.maxRows = maxRows;
        this.history = new ResultSetHistory(maxHistorySize);
        this.setModel(tableSorter);

        tableSorter.addMouseListenerToHeaderInTable(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * .........................................................................
     */
    public void reset() {
        this.tableSorter.setModel(new DefaultTableModel());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   resultSet  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void update(final ResultSet resultSet) throws SQLException {
        this.update(resultSet, -1, -1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   resultSet  DOCUMENT ME!
     * @param   index      DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void update(final ResultSet resultSet, final int index) throws SQLException {
        this.update(resultSet, -1, index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   resultSet  DOCUMENT ME!
     * @param   maxRows    DOCUMENT ME!
     * @param   index      DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void update(final ResultSet resultSet, final int maxRows, final int index) throws SQLException {
        if (maxRows != -1) {
            this.setMaxRows(maxRows);
        }

        final ResultSetMetaData metaData = resultSet.getMetaData();
        final String[] columnNames = new String[metaData.getColumnCount()];

        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = metaData.getColumnName(i + 1);
        }

        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        int i = 1;

        while (resultSet.next() && (i < this.maxRows)) {
            final Object[] objects = new Object[metaData.getColumnCount()];
            for (int j = 0; j < objects.length; j++) {
                objects[j] = resultSet.getObject(j + 1);
            }

            tableModel.addRow(objects);
            i++;
        }

        this.history.setHistoryEntry(tableModel, index);
        this.update(tableModel);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tableModel  DOCUMENT ME!
     */
    protected void update(final DefaultTableModel tableModel) {
        this.tableSorter.setModel(tableModel);

        if (this.getColumnCount() > 8) {
            this.setAutoResizeMode(AUTO_RESIZE_OFF);
            final Enumeration enum_ = this.getColumnModel().getColumns();
            while (enum_.hasMoreElements()) {
                ((TableColumn)enum_.nextElement()).setMinWidth(150);
            }
        } else {
            this.setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public History getHistory() {
        return this.history;
    }

    /**
     * Getter for property maxRows.
     *
     * @return  Value of property maxRows.
     */
    public int getMaxRows() {
        return this.maxRows;
    }

    /**
     * Setter for property maxRows.
     *
     * @param  maxRows  New value of property maxRows.
     */
    public void setMaxRows(final int maxRows) {
        this.maxRows = (maxRows > 0) ? maxRows : 1;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ResultSetTableSorter extends de.cismet.common.gui.misc.TableSorter {

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ResultSetHistory extends AbstractHistory {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ResultSetHistory object.
         *
         * @param  maxHistorySize  DOCUMENT ME!
         */
        public ResultSetHistory(final int maxHistorySize) {
            super(maxHistorySize);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void setSelectedIndex(final int selectedIndex) {
            if ((selectedIndex >= 0) && (this.selectedIndex != selectedIndex)
                        && (selectedIndex < this.maxHistorySize)) {
                Object object = null;
                if ((selectedIndex < this.historyList.size())
                            && ((object = this.historyList.get(selectedIndex)) != null)) {
                    ResultSetTable.this.update(((DefaultTableModel)object));
                } else {
                    ResultSetTable.this.reset();
                }

                this.selectedIndex = selectedIndex;
            }
        }

        @Override
        public void addPropertyChangeListener(final java.beans.PropertyChangeListener l) {
            // disabled
        }

        @Override
        public void removePropertyChangeListener(final java.beans.PropertyChangeListener l) {
            // disabled
        }
    }
}
