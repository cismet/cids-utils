/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultHistory.java
 *
 * Created on 9. September 2003, 16:15
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
public class DefaultHistory extends AbstractHistory {

    //~ Instance fields --------------------------------------------------------

    protected final PropertyChangeSupport propertyChangeSupport;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of DefaultHistory.
     */
    public DefaultHistory() {
        this(8);
    }

    /**
     * Creates a new DefaultHistory object.
     *
     * @param  maxHistorySize  DOCUMENT ME!
     */
    public DefaultHistory(final int maxHistorySize) {
        super(maxHistorySize);
        this.propertyChangeSupport = new SwingPropertyChangeSupport(this);
        this.historyList = new LinkedList();
    }

    /**
     * Creates a new DefaultHistory object.
     *
     * @param  maxHistorySize         DOCUMENT ME!
     * @param  propertyChangeSupport  DOCUMENT ME!
     */
    public DefaultHistory(final int maxHistorySize, final PropertyChangeSupport propertyChangeSupport) {
        super(maxHistorySize);
        this.propertyChangeSupport = propertyChangeSupport;
        this.historyList = new LinkedList();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setSelectedIndex(final int selectedIndex) {
        if ((selectedIndex >= 0) && (this.selectedIndex != selectedIndex) && (selectedIndex < this.historyList.size())
                    && (this.historyList.get(selectedIndex) != null)) {
            final int oldSelectedIndex = this.selectedIndex;
            this.selectedIndex = selectedIndex;
            this.propertyChangeSupport.firePropertyChange(
                SELECTED_HISTORY_ENTRY,
                new Integer(oldSelectedIndex),
                new Integer(selectedIndex));
        }
    }

    @Override
    public void removePropertyChangeListener(final java.beans.PropertyChangeListener l) {
        this.propertyChangeSupport.removePropertyChangeListener(l);
    }

    @Override
    public void addPropertyChangeListener(final java.beans.PropertyChangeListener l) {
        this.propertyChangeSupport.addPropertyChangeListener(l);
    }
}
