/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AbstractHistory.java
 *
 * Created on 9. September 2003, 16:43
 */
package de.cismet.common.gui.sqlwizard;

import java.beans.*;

import java.util.*;

import javax.swing.event.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class AbstractHistory implements History {

    //~ Instance fields --------------------------------------------------------

    protected int maxHistorySize = 0;
    protected LinkedList historyList;

    protected int selectedIndex = -1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractHistory object.
     *
     * @param  maxHistorySize  DOCUMENT ME!
     */
    public AbstractHistory(final int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
        this.historyList = new LinkedList();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void clearHistory() {
        this.historyList.clear();
        this.selectedIndex = -1;
    }

    @Override
    public int getHistorySize() {
        return this.historyList.size();
    }

    @Override
    public int getMaxHistorySize() {
        return this.maxHistorySize;
    }

    @Override
    public abstract void setSelectedIndex(int selectedIndex);

    @Override
    public void addHistoryEntry(final Object historyEntry) {
        this.setHistoryEntry(historyEntry, -1);
    }

    @Override
    public void setHistoryEntry(final Object historyEntry, final int index) {
        if (index == -1) {
            if (this.maxHistorySize > 0) {
                if (this.historyList.size() == this.maxHistorySize) {
                    this.historyList.removeFirst();
                }

                this.historyList.add(historyEntry);
                this.selectedIndex = this.historyList.size() - 1;
            }
        } else {
            if (this.maxHistorySize > 0) {
                if (index >= this.historyList.size()) {
                    int i = this.historyList.size();
                    for (; i <= index; i++) {
                        // System.out.println("add" + i + " " + index);
                        this.historyList.add(null);
                    }
                }

                this.historyList.set(index, historyEntry);
                this.selectedIndex = index;
            }
        }
    }

    @Override
    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    @Override
    public Object getHistroyEntry(final int index) {
        return this.historyList.get(index);
    }

    @Override
    public Object getSelectedHistoryEntry() {
        return this.historyList.get(this.selectedIndex);
    }

    @Override
    public java.util.Iterator getHistoryEntries() {
        return this.historyList.iterator();
    }

    @Override
    public abstract void removePropertyChangeListener(java.beans.PropertyChangeListener l);
    @Override
    public abstract void addPropertyChangeListener(java.beans.PropertyChangeListener l);
}
