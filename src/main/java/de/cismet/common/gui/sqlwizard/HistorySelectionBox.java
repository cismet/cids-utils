/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * HistorySelectionBox.java
 *
 * Created on 8. September 2003, 14:40
 */
package de.cismet.common.gui.sqlwizard;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.util.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class HistorySelectionBox extends JComboBox {

    //~ Instance fields --------------------------------------------------------

    private final LinkedList historySupportList;
    // private final PropertyChangeListener historyObserver;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of HistorySelectionBox.
     *
     * <p>Creates five default entries (Query #n).</p>
     */
    public HistorySelectionBox() {
        this(new String[] { "Query #1", "Query #2", "Query #3", "Query #4", "Query #5" });
    }

    /**
     * Creates a new instance of HistorySelectionBox.
     *
     * @param  entries  the default entries of this box.
     */
    public HistorySelectionBox(final String[] entries) {
        super(entries);

        this.historySupportList = new LinkedList();
        // this.historyObserver = new HistoryObserver();

        this.setRenderer(new HistoryListCellRenderer());
        this.addItemListener(new HistorySelectionListener());

        // no good idea:
        // this.setMaximumWitdh(175);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Sets new entries of this box.
     *
     * <p>Clears all registred history lists.</p>
     *
     * @param  entries  the new entries of this box.
     */
    public void update(final String[] entries) {
        this.setModel(new DefaultComboBoxModel(entries));
        this.setSelectedIndex(-1);

        final Iterator iterator = this.historySupportList.iterator();
        while (iterator.hasNext()) {
            ((History)iterator.next()).clearHistory();
        }
    }

    /**
     * Sets the maximum witdh of this box (in pixels).
     *
     * @param       width  the new maximum witdh of this box
     *
     * @deprecated  don't use this method
     */
    protected void setMaximumWitdh(final int width) {
        final Dimension dimension = this.getPreferredSize();
        dimension.width = width;
        this.setPreferredSize(dimension);
        this.setMaximumSize(getPreferredSize());
    }

    /**
     * Selectes a new history entry.
     *
     * @param  index  the index of the history entry in the history list
     */
    public void setSelectedHistoryEntry(final int index) {
        if (index < this.getItemCount()) {
            this.setSelectedIndex(index);
            this.selectHistoryEntry(index);

            /*if(this.getSelectedIndex() != index)
             * { this.setSelectedIndex(index); } else { this.selectHistroyEntry(index);} */
        }
    }

    /**
     * Selectes a new history entry, notifies all history objects.
     *
     * @param  name  the name of the history object.
     */
    public void setSelectedHistoryEntry(final String name) {
        for (int i = 0; i < this.getItemCount(); i++) {
            if (this.getItemAt(i).toString().equalsIgnoreCase(name)) {
                this.setSelectedHistoryEntry(i);
                break;
            }
        }
    }

    /**
     * Adds a new history to the history list.
     *
     * @param   history  a new history object.
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    protected void addHistory(final History history) throws IllegalArgumentException {
        if (history.getMaxHistorySize() < this.getItemCount()) {
            throw new IllegalArgumentException("max history size: " + history.getMaxHistorySize()
                        + " dos not match history box size: " + this.getItemCount());
        } else if (!this.historySupportList.contains(history)) {
            // history.addPropertyChangeListener(this.historyObserver);
            this.historySupportList.add(history);
        }
    }

    /**
     * Adds a new history to the history list.
     *
     * @param  history  a new history object.
     */
    protected void removeHistory(final History history) {
        if (this.historySupportList.contains(history)) {
            // history.removePropertyChangeListener(this.historyObserver);
            this.historySupportList.remove(history);
        }
    }

    /**
     * Selectes a new history entry, notifies all history objects.
     *
     * @param  selectedHistoryEntry  DOCUMENT ME!
     */
    private void selectHistoryEntry(final int selectedHistoryEntry) {
        // System.out.println("selectHistoryEntry " + selectedHistroyEntry);
        final Iterator iterator = this.historySupportList.iterator();
        while (iterator.hasNext()) {
            ((History)iterator.next()).setSelectedIndex(selectedHistoryEntry);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isValid(final int index) {
        final Iterator iterator = this.historySupportList.iterator();
        while (iterator.hasNext()) {
            final History history = ((History)iterator.next());
            if ((index < 0) || (index >= history.getHistorySize()) || (history.getHistroyEntry(index) == null)) {
                return false;
            }
        }

        return true;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * -------------------------------------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    private class HistorySelectionListener implements ItemListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // System.out.println("itemStateChanged " + getSelectedIndex());
                selectHistoryEntry(HistorySelectionBox.this.getSelectedIndex());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class HistoryListCellRenderer extends DefaultListCellRenderer {

        //~ Instance fields ----------------------------------------------------

        protected final ImageIcon validIcon;
        protected final ImageIcon invalidIcon;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new HistoryListCellRenderer object.
         */
        protected HistoryListCellRenderer() {
            super();

            validIcon = new ImageIcon(this.getClass().getResource("/de/cismet/common/gui/sqlwizard/images/valid.gif"));
            invalidIcon = new ImageIcon(this.getClass().getResource(
                        "/de/cismet/common/gui/sqlwizard/images/invalid.gif"));
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Component getListCellRendererComponent(final JList list,
                final Object value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
            final JLabel label = (JLabel)super.getListCellRendererComponent(
                    list,
                    value,
                    index,
                    isSelected,
                    cellHasFocus);
            final int selectedIndex = (index > -1) ? index : HistorySelectionBox.this.getSelectedIndex();

            if (HistorySelectionBox.this.isValid(selectedIndex)) {
                label.setIcon(validIcon);
            } else {
                label.setIcon(invalidIcon);
            }

            return label;
        }
    }

    /*private class HistoryObserver implements PropertyChangeListener
     * { public void propertyChange(PropertyChangeEvent evt) {
     * if(evt.getPropertyName().equals(History.SELECTED_HISTORY_ENTRY))     {
     * selectHistoryEntry(((Integer)evt.getNewValue()).intValue());     } }   }*/
}
