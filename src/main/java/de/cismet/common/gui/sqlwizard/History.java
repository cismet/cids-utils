/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * HistorySupport.java
 *
 * Created on 8. September 2003, 14:44
 */
package de.cismet.common.gui.sqlwizard;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface History {

    //~ Static fields/initializers ---------------------------------------------

    String SELECTED_HISTORY_ENTRY = "selectedHistoryEntry";

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getHistroyEntry(int index);

    /**
     * DOCUMENT ME!
     *
     * @param  historyEntry  DOCUMENT ME!
     * @param  index         DOCUMENT ME!
     */
    void setHistoryEntry(Object historyEntry, int index);

    /**
     * DOCUMENT ME!
     *
     * @param  historyEntry  DOCUMENT ME!
     */
    void addHistoryEntry(Object historyEntry);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getSelectedHistoryEntry();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getSelectedIndex();

    /**
     * DOCUMENT ME!
     *
     * @param  index  DOCUMENT ME!
     */
    void setSelectedIndex(int index);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getHistorySize();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getMaxHistorySize();

    /**
     * DOCUMENT ME!
     */
    void clearHistory();

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    void addPropertyChangeListener(java.beans.PropertyChangeListener l);

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    void removePropertyChangeListener(java.beans.PropertyChangeListener l);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    java.util.Iterator getHistoryEntries();
}
