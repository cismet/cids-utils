/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SQLEditor.java
 *
 * Created on 4. September 2003, 15:05
 */
package de.cismet.common.gui.sqlwizard;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;

import java.io.*;

import java.sql.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

//import org.apache.log4j.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SQLEditor extends JTextPane {

    //~ Instance fields --------------------------------------------------------

    private final SQLEditorHistory history;

    // private final Logger logger;
    /** Holds value of property maxHistorySize. */
    // private final int maxHistorySize;

    /** Holds value of property selectedHistoryEntry. */
    // private int selectedHistoryEntry;

    /** Holds value of property maxRows. */
    private int maxRows = 75;

    /** Holds value of property statement. */
    private Statement statement = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Utility field used by bound properties.
     *
     * @param  maxHistorySize  DOCUMENT ME!
     * @param  maxRows         DOCUMENT ME!
     */
    // private final SwingPropertyChangeSupport propertyChangeSupport =  new SwingPropertyChangeSupport(this);

    /**
     * Creates new form SQLEditor.
     *
     * @param  maxHistorySize  DOCUMENT ME!
     * @param  maxRows         DOCUMENT ME!
     */
    public SQLEditor(final int maxHistorySize, final int maxRows) {
        // this.maxHistorySize = maxHistorySize;
        this.maxRows = maxRows;
        this.history = new SQLEditorHistory(maxHistorySize);
        // unsinn: this.setTransferHandler(new DatabaseMetaDataTransferHandler()); this.logger =
        // Logger.getLogger(this.getClass());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isEmpty() {
        return (this.statement == null) || (this.getDocument().getLength() <= 0);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        this.setText(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public ResultSet execute() throws SQLException {
        return this.execute(-1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public ResultSet execute(final int index) throws SQLException {
        if (!this.isEmpty()) {
            this.history.setHistoryEntry(this.getText(), index);

            try {
                this.statement.setMaxRows(this.maxRows);
            } catch (SQLException sqlexp) {
            }

            /*(this.statement.execute(this.getText()))
             * { return this.statement.getResultSet();}*/

            return this.statement.executeQuery(this.getText());
        }

        return null;
    }

    /**
     * .........................................................................
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
        this.maxRows = maxRows;
    }

    /**
     * Getter for property statement.
     *
     * @return  Value of property statement.
     */
    public Statement getStatement() {
        return this.statement;
    }

    /**
     * Setter for property statement.
     *
     * @param  statement  New value of property statement.
     */
    public void setStatement(final Statement statement) {
        this.statement = statement;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class SQLEditorHistory extends AbstractHistory {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SQLEditorHistory object.
         *
         * @param  maxHistorySize  DOCUMENT ME!
         */
        public SQLEditorHistory(final int maxHistorySize) {
            super(maxHistorySize);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void setSelectedIndex(final int selectedIndex) {
            // System.out.println("selectedIndex " + selectedIndex); System.out.println("this.selectedIndex " +
            // this.selectedIndex); System.out.println("this.maxHistorySize " + this.maxHistorySize);
            // System.out.println("this.historyList.size() " + this.historyList.size());
            // System.out.println(".......................................................");

            if ((selectedIndex >= 0) && (this.selectedIndex != selectedIndex)
                        && (selectedIndex < this.maxHistorySize)) {
                Object object = null;
                if ((selectedIndex < this.historyList.size())
                            && ((object = this.historyList.get(selectedIndex)) != null)) {
                    SQLEditor.this.setText(object.toString());
                } else {
                    SQLEditor.this.reset();
                }

                final int oldSelectedHistoryEntry = this.selectedIndex;
                this.selectedIndex = selectedIndex;

                // disabled firePropertyChange(SELECTED_HISTORY_ENTRY, new Integer(oldSelectedHistoryEntry), new
                // Integer(selectedIndex));
            }
        }

        @Override
        public void addPropertyChangeListener(final java.beans.PropertyChangeListener l) {
            // disabled
            // SQLEditor.this.addPropertyChangeListener(l);
        }

        @Override
        public void removePropertyChangeListener(final java.beans.PropertyChangeListener l) {
            // disabled
            // SQLEditor.this.removePropertyChangeListener(l);
        }
    }

    /*private class DatabaseMetaDataTransferHandler extends TransferHandler
     * { public void exportToClipboard(JComponent comp, Clipboard clipboard, int action) {     if (comp instanceof
     * JTextComponent)     {         JTextComponent text = (JTextComponent)comp;         int p0 =
     * text.getSelectionStart();         int p1 = text.getSelectionEnd();         if (p0 != p1)         {
     * try             {                 Document doc = text.getDocument();                 String srcData =
     * doc.getText(p0, p1 - p0);                 StringSelection contents =new StringSelection(srcData);
     * clipboard.setContents(contents, null);                 if (action == TransferHandler.MOVE)                 {
     *                doc.remove(p0, p1 - p0);                 }             } catch (BadLocationException ble)
     *    {}         }     } } public boolean importData(JComponent comp, Transferable t) {     if (comp instanceof
     * JTextComponent)     {         DataFlavor flavor = getFlavor(t.getTransferDataFlavors());
     * System.out.println(t);                           if (flavor != null)         {             try             {
     *            String data = "$" + (String)t.getTransferData(flavor) + "$";
     * ((JTextComponent)comp).replaceSelection(data);                 return true;             }              catch
     * (UnsupportedFlavorException ufe)             {             }              catch (IOException ioe)             {
     *           }         }     }     return false; }  public boolean canImport(JComponent comp, DataFlavor[]
     * transferFlavors) {     JTextComponent c = (JTextComponent)comp;     if (!(c.isEditable() && c.isEnabled()))     {
     *         return false;     }     return (getFlavor(transferFlavors) != null); }  public int
     * getSourceActions(JComponent c) {     return NONE; }  private DataFlavor getFlavor(DataFlavor[] flavors) {     if
     * (flavors != null)     {         for (int counter = 0; counter < flavors.length; counter++)         {
     * System.out.println(flavors[counter]);             if (flavors[counter].equals(DataFlavor.stringFlavor))
     *   {                 return flavors[counter];             }         }     }     return null; }}*/
}
