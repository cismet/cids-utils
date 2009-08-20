/*
 * HistorySupport.java
 *
 * Created on 8. September 2003, 14:44
 */

package de.cismet.common.gui.sqlwizard;

/**
 *
 * @author  pascal
 */
public interface History
{
    public final static String SELECTED_HISTORY_ENTRY = "selectedHistoryEntry";
    
    
    public Object getHistroyEntry(int index);
    
    public void setHistoryEntry(Object historyEntry, int index);
    
    public void addHistoryEntry(Object historyEntry);
    
    
    public Object getSelectedHistoryEntry();
    
    public int getSelectedIndex();
    
    public void setSelectedIndex(int index);
    
    
    public int getHistorySize();
    
    public int getMaxHistorySize();
    
    public void clearHistory();
    
    
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l);
    
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l);
    
    public java.util.Iterator getHistoryEntries();
    
}
